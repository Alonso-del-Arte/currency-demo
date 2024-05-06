/*
 * Copyright (C) 2024 Alonso del Arte
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
 * Chooses currencies to be used in the testing of other classes in the 
 * <code>currency</code> package. Functions are provided to choose currencies 
 * pseudorandomly, choose currencies with a specified number of subdivisions 
 * (e.g., 100 cents, 1,000 mills), and to choose currencies other than a 
 * specified currency.
 * @author Alonso del Arte
 */
public class CurrencyChooser {
    
    static final Random RANDOM = new Random();
    
    private static final List<Currency> CURRENCIES 
            = new ArrayList<>(Currency.getAvailableCurrencies());

    private static final Set<Currency> PSEUDO_CURRENCIES = new HashSet<>();

    private static final Set<Currency> HISTORICAL_CURRENCIES = new HashSet<>();

    private static final Set<Currency> OTHER_EXCLUSIONS = new HashSet<>();
    
    private static final String[] OTHER_EXCLUSION_CODES = {"ADP", "BGL", "BOV", 
        "CHW", "COU", "EEK", "FIM", "GRD", "ITL", "PTE", "SIT", "USN", "USS", 
        "UYI"};

    private static final Map<Integer, Set<Currency>> CURRENCIES_DIGITS_MAP 
            = new HashMap<>();
    
    static {
        final String nineteenthCenturyYearIndicator = "\u002818";
        final String twentiethCenturyYearIndicator = "\u002819";
        final String twentyFirstCenturyYearIndicator = "\u002820";
        for (Currency currency : CURRENCIES) {
            int fractionDigits = currency.getDefaultFractionDigits(); 
            if (fractionDigits < 0) {
                PSEUDO_CURRENCIES.add(currency);
            } else {
                String dispName = currency.getDisplayName();
                if (dispName.contains(nineteenthCenturyYearIndicator)
                        || dispName.contains(twentiethCenturyYearIndicator) 
                        || dispName.contains(twentyFirstCenturyYearIndicator)) 
                {
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
     * Gives the set of currencies suitable for the {@link MoneyAmount}  
     * constructor. Hopefully all these currencies are also suitable for online 
     * foreign exchange rate APIs.
     * @return A set of all available currencies, minus historical currencies 
     * such as the original bol&iacute;var (VEB, 1871 &mdash; 2008) or the old 
     * bol&iacute;var (VEF, 2008 &mdash; 2018) which was replaced by the 
     * bol&iacute;var soverano (VED), and also excluding metals like gold (XAU) 
     * and silver (XAG).
     */
    public static Set<Currency> getSuitableCurrencies() {
        return new HashSet<>(CURRENCIES);
    }

    /**
     * Chooses a currency suitable for the {@link MoneyAmount} constructor. Also 
     * tries not to choose a currency that might not be supported by an online 
     * currency conversion API, such as historical currencies (like the old 
     * Russian ruble, RUR) that are marked as historical in the Java runtime's 
     * currency information file by having a range of years in their display 
     * names (e.g., 1991 &mdash; 1998 for the old Russian ruble), and the 
     * following specific currencies:
     * <ul>
     * <li>The Bulgarian hard lev (BGL), I'm not sure how it differs from the 
     * Bulgarian lev (BGN).</li>
     * <li>The Bolivian MVDOL (BOV), a monetary unit managed by the Banco 
     * Central de Bolivia. The MVDOL correlates the modern boliviano (BOB) to 
     * the United States dollar (USD). The MVDOL is used for financial 
     * instruments like treasury bills. From what I understand, it's not used 
     * for everyday expenses like buying groceries, nor for tourists' expenses, 
     * like booking a hotel.</li>
     * <li>The WIR franc (CHW), a "community currency" that is equal in value to 
     * the Swiss franc (CHF).</li>
     * <li>The same day U.&nbsp;S. dollar (USS) and the next day U.&nbsp;S. 
     * dollar (USN), which serve special purposes in some contexts but are 
     * generally not recognized by currency conversion APIs.</li>
     * </ul>
     * <p>Also, the former currencies of the European nations that now use the 
     * euro (EUR) are not marked as historical in the currency information file. 
     * Specifically, but probably not limited to:</p>
     * <ul>
     * <li>The Andorran peseta (ADP) was a currency of Andorra in the 
     * 20<sup>th</sup> Century. It was pegged to the Spanish peseta (ESP) at 
     * 1:1. By 2002 it had been phased out in favor of the euro.</li>
     * <li>The Estonian kroon (EEK) was the official currency of Estonia from 
     * 1992 to 2011.</li>
     * <li>The Finnish markka (FIM) was the official currency of Finland from 
     * 1860 to 2002.</li>
     * <li>The Italian lira (ITL) was the only official currency of Italy from 
     * 1861 to 1999. It was phased out by 2002, as Italians got used to the 
     * euro.</li>
     * <li>The Portuguese escudo was the official currency of Portugal from 1911 
     * to 2002. Portugal was actually one of the first nations of the European 
     * Union to adopt the euro, in 1999.</li>
     * <li>The Slovenian tolar (SIT) was the official currency of Slovenia from 
     * 1991 to 2007.</li>
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
