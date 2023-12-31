/*
 * Copyright (C) 2023 Alonso del Arte
 *
 * This program is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation, either version 3 of the License, or (at your option) any later 
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package currency;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Alonso del Arte
 */
public class CurrencyChooser {
    
    static final Random RANDOM = new Random();
    
    private static final List<Currency> CURRENCIES 
            = new ArrayList<>(Currency.getAvailableCurrencies());

    private static final Set<Currency> PSEUDO_CURRENCIES = new HashSet<>();

    private static final Set<Currency> HISTORICAL_CURRENCIES = new HashSet<>();

    private static final Set<Currency> OTHER_EXCLUSIONS = new HashSet<>();
    
    private static final String[] OTHER_EXCLUSION_CODES = {"BGL", "CHW", "EEK", 
        "FIM", "SIT", "USS"};

    private static final Map<Integer, Set<Currency>> CURRENCIES_DIGITS_MAP 
            = new HashMap<>();
    
    static {
        final String twentiethCenturyYearIndicator = "\u002819";
        final String twentyFirstCenturyYearIndicator = "\u002820";
        for (Currency currency : CURRENCIES) {
            int fractionDigits = currency.getDefaultFractionDigits(); 
            if (fractionDigits < 0) {
                PSEUDO_CURRENCIES.add(currency);
            } else {
                String dispName = currency.getDisplayName();
                if (dispName.contains(twentiethCenturyYearIndicator) 
                        || dispName.contains(twentyFirstCenturyYearIndicator)) {
                    HISTORICAL_CURRENCIES.add(currency);
                } else {
                    Set<Currency> digitGroupedSet;
                    if (CURRENCIES_DIGITS_MAP.containsKey(fractionDigits)) {
                        digitGroupedSet = CURRENCIES_DIGITS_MAP
                                .get(fractionDigits);
                    } else {
                        digitGroupedSet = new HashSet<>();
                        CURRENCIES_DIGITS_MAP.put(fractionDigits, 
                                digitGroupedSet);
                    }
                    digitGroupedSet.add(currency);
                }
            }
        }
        for (String exclusionCode : OTHER_EXCLUSION_CODES) {
            try {
                Currency currency = Currency.getInstance(exclusionCode);
                OTHER_EXCLUSIONS.add(currency);
            } catch (IllegalArgumentException iae) {
                System.err.println("\"" + iae.getMessage() + "\"");
            }
        }
        CURRENCIES.removeAll(PSEUDO_CURRENCIES);
        CURRENCIES.removeAll(HISTORICAL_CURRENCIES);
        CURRENCIES.removeAll(OTHER_EXCLUSIONS);
    }

    /**
     * Chooses a currency suitable for the {@link MoneyAmount} constructor. Also 
     * tries not to choose a currency that might not be supported by an online 
     * currency conversion API, such as historical currencies (like the old 
     * Russian ruble) and the following specific currencies:
     * <ul>
     * <li>The Bulgarian hard lev (BGL), not sure how it differs from the 
     * Bulgarian lev (BGN)</li>
     * <li>The WIR franc (CHW), a "community currency" that is equal in value to 
     * the Swiss franc (CHF)</li>
     * <li>The Estonian kroon (EEK), technically a historical currency since now 
     * the euro (EUR) is the only official currency in Estonia, but it might not 
     * get picked up as such if the currency file data doesn't indicate 
     * something like "(1992 &mdash; 2011)"</li>
     * <li>The Slovenian tolar (SIT), technically a historical currency since 
     * now the euro (EUR) is the only official currency in Slovenia, but it 
     * might not get picked up as such if the currency file data doesn't 
     * indicate something like "(1991 &mdash; 2007)"</li>
     * <li>The same day U.&nbsp;S. dollar (USS), not sure how it differs from 
     * the U.&nbsp;S. dollar (USD)</li>
     * </ul>
     * @return A currency with default fraction digits of at least 0. For 
     * example, the Kyrgystani som (KGS), which like most world currencies by 
     * default has two fractional digits. A som is divided into 100 tyin.
     */
    public static Currency chooseCurrency() {
        int index = RANDOM.nextInt(CURRENCIES.size());
        return CURRENCIES.get(index);
    }

    /**
     * Chooses a currency with a specified number of default fraction digits. 
     * Thus, a currency that divides into 100 cents (corresponding to 2 default 
     * fraction digits) can be chosen.
     * @param fractionDigits How many fraction digits the currency should have. 
     * Examples: 2, 3, 0, 4, 7, &minus;6.
     * @return A currency with the specified number of fraction digits. 
     * Examples: For 2, the Guatemalan quetzal (GTQ), one of which divides into 
     * 100 centavos; for 3, the Omani rial (OMR), one of which divides into 
     * 1,000 baisa; for 0, the Luxembourgian franc, one of which does not 
     * normally divide into any kind of cent, unlike the Swiss franc; and for 4, 
     * the only available option might be the Chilean unit of account (CLF), 
     * which, however, does not have any kind of circulating bills or coins 
     * associated with it. 
     * @throws NoSuchElementException If there are no currencies for 
     * <code>fractionDigits</code>. In the examples given above, 7 would almost 
     * certainly cause this exception, as such a currency is unlikely to be on 
     * the list of available currencies, and &minus;6 definitely would, as 
     * <code>Currency</code> instances with negative default fraction digits are 
     * ignored by this chooser.
     */
    public static Currency chooseCurrency(int fractionDigits) {
        if (CURRENCIES_DIGITS_MAP.containsKey(fractionDigits)) {
            List<Currency> currencies 
                    = new ArrayList<>(CURRENCIES_DIGITS_MAP
                            .get(fractionDigits));
            int index = RANDOM.nextInt(currencies.size());
            return currencies.get(index);
        } else {
            String excMsg = "No available currency with " + fractionDigits 
                    + " fraction digits";
            throw new NoSuchElementException(excMsg);
        }
    }

    /**
     * Chooses a currency suitable for the {@link MoneyAmount} constructor 
     * other than the specified currency.
     * @param currency The currency not to choose. For example, the U.&nbsp;S. 
     * dollar (USD). Pseudo-currencies like gold (XAU) may be used, but then the 
     * effect is the same as calling {@link #chooseCurrency()} without any 
     * parameters.
     * @return A currency other than <code>currency</code>. For example, given 
     * USD, this function might return the Panamanian balboa (PAB). For what 
     * it's worth, the balboa is one of two official currencies of Panama, the 
     * other is the U.&nbsp;S. dollar.
     */
    public static Currency chooseCurrencyOtherThan(Currency currency) {
        Currency otherCurrency = currency;
        while (otherCurrency == currency) {
            otherCurrency = chooseCurrency();
        }
        return otherCurrency;
    }

}
