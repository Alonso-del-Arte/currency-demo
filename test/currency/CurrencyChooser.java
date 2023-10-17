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
                Set<Currency> digitGroupedSet;
                if (CURRENCIES_DIGITS_MAP.containsKey(fractionDigits)) {
                    digitGroupedSet = CURRENCIES_DIGITS_MAP.get(fractionDigits);
                } else {
                    digitGroupedSet = new HashSet<>();
                    CURRENCIES_DIGITS_MAP.put(fractionDigits, digitGroupedSet);
                }
                digitGroupedSet.add(currency);
            }
            String dispName = currency.getDisplayName();
            if (dispName.contains(twentiethCenturyYearIndicator) 
                    || dispName.contains(twentyFirstCenturyYearIndicator)) {
                HISTORICAL_CURRENCIES.add(currency);
            }
        }
        CURRENCIES.removeAll(PSEUDO_CURRENCIES);
        CURRENCIES.removeAll(HISTORICAL_CURRENCIES);
    }

    /**
     * Chooses a currency suitable for the {@link MoneyAmount} constructor.
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
