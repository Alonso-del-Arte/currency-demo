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
import java.util.Arrays;
import java.util.Currency;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

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
    
    private static final List<Currency> PSEUDO_CURRENCIES_LIST;

    private static final Set<Currency> HISTORICAL_CURRENCIES = new HashSet<>();

    private static final Set<Currency> OTHER_EXCLUSIONS = new HashSet<>();
    
    private static final String[] OTHER_EXCLUSION_CODES = {"ADP", "ATS", "AYM", 
        "BEF", "BGL", "BOV", "CHE", "CHW", "COU", "CYP", "DEM", "EEK", "ESP", 
        "FIM", "FRF", "GRD", "GWP", "IEP", "ITL", "LUF", "MTL", "MXV", "NLG", 
        "PTE", "SIT", "SRG", "STN", "TPE", "USN", "USS", "UYI", "VED", "ZWN"};

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
                    if (Arrays.binarySearch(OTHER_EXCLUSION_CODES, 
                            currency.getCurrencyCode()) < 0) {
                        digitGroupedSet.add(currency);
                    }
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
        PSEUDO_CURRENCIES_LIST = new ArrayList<>(PSEUDO_CURRENCIES);
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
     * Tells whether or not a currency is suitable for the {@link MoneyAmount} 
     * constructor and {@link CurrencyConverter}. If a currency's identified as 
     * historical in the Java runtime currency information file, or if it's a 
     * precious metal (e.g., gold, silver, platinum), or if it's for testing 
     * purposes only, it will not be considered suitable. There are a few other 
     * exclusions.
     * @param currency The currency to check. Two examples: the old Venezuelan 
     * bol&iacute;var (VEB), which was valid from 1871 to 2008; and the modern 
     * Venezuelan bol&iacute;var (VES), technically valid since 2018 &mdash; for 
     * what it's worth, however, you might be better off with U.&nbsp;S. dollars 
     * in Venezuela.
     * @return True if the currency's suitable, false otherwise. In the 
     * examples, this function returns false for VEB (currency conversion APIs 
     * don't give exchange rates for conversions to and from this currency) and 
     * true for VES (one euro exchanges to 39.65 bol&iacute;vares as of May 21, 
     * 2024).
     */
    public static boolean isSuitableCurrency(Currency currency) {
        return CURRENCIES.contains(currency);
    }
    
    /**
     * Chooses a pseudocurrency. Pseudocurrencies differ from standard 
     * currencies in that their values are not determined by a government bank.
     * @return A pseudocurrency. For example, gold (XAU).
     */
    public static Currency choosePseudocurrency() {
        int index = RANDOM.nextInt(PSEUDO_CURRENCIES_LIST.size());
        return PSEUDO_CURRENCIES_LIST.get(index);
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
     * <li>The Azerbaijan manat was improperly given the currency code AYM in 
     * 2005. The ISO-4217 rules require that the 3-letter code start with the 
     * 2-letter ISO-3166 code for the country, in this case AZ. It was removed 
     * the next year, but remains in Java's currency information file.</li>
     * <li>The Bulgarian hard lev (BGL), I'm not sure how it differs from the 
     * Bulgarian lev (BGN).</li>
     * <li>The Bolivian MVDOL (BOV), a monetary unit managed by the Banco 
     * Central de Bolivia. The MVDOL correlates the modern boliviano (BOB) to 
     * the United States dollar (USD). The MVDOL is used for financial 
     * instruments like treasury bills. From what I understand, it's not used 
     * for everyday expenses like buying groceries, nor for tourists' expenses, 
     * like booking a hotel.</li>
     * <li>The WIR euro (CHE), a "community currency" that is equal in value to 
     * the euro (EUR), but is not traded outside of Switzerland.</li>
     * <li>The WIR franc (CHW), a "community currency" that is equal in value to 
     * the Swiss franc (CHF).</li>
     * <li>The Guinea-Bissau peso (GWP) was the official currency of 
     * Guinea-Bissau from 1976 to 1997. It was originally traded at par with the 
     * Portuguese escudo (PTE), but it was already overvalued at that rate. In 
     * 1997, it was replaced by the CFA franc.</li>
     * <li>The Surinamese guilder (SRG) was the currency of Suriname until 2004, 
     * when it was replaced by the Surinamese dollar (SRD) at a rate of a 
     * thousand guilder for one dollar.</li>
     * <li>The S&atilde;o Tom&eacute; &amp; Pr&iacute;ncipe dobra (STN) is still 
     * a valid currency, as far as I can tell, but Manny's Currency Converter 
     * does not recognize it, instead recognizing the earlier dobra (STD), which 
     * was valid from 1977 to 2017.</li>
     * <li>The Timorese escudo (TPE) was the official currency of Portuguese 
     * Timor prior to occupation by Indonesia.</li>
     * <li>The same day U.&nbsp;S. dollar (USS) and the next day U.&nbsp;S. 
     * dollar (USN), which serve special purposes in some contexts but are 
     * generally not recognized by currency conversion APIs.</li>
     * <li>The so-called "bolivar dig&iacute;tal is excluded because, despite 
     * its official standing, is not recognized by some currency conversion 
     * APIs.</li>
     * <li>The Zimbabwean dollar (ZWN) is in limbo as far as Java's runtime is 
     * concerned. Although another Zimbabwean dollar (ZWL) is recognized by 
     * Manny's free currency conversion API, Java's currency information file 
     * lists it as a historical currency that was valid only in 2009. Whatever 
     * the case may be, tourists are perhaps best served bringing U.&nbsp;S. 
     * dollars.</li>
     * </ul>
     * <p>Also, the former currencies of the European nations that now use the 
     * euro (EUR) are not marked as historical in the currency information file. 
     * In the case of the founding nations of the eurozone, the euro was 
     * introduced in 1999 but the old national currency continued to be valid 
     * until 2002. Some of the old notes and coins can still be exchanged to 
     * euros, but this varies from country to country. These are the 
     * euro-replaced currencies this currency chooser specifically excludes:</p>
     * <ul>
     * <li>The Andorran peseta (ADP) was a currency of Andorra in the 
     * 20<sup>th</sup> Century. It was pegged to the Spanish peseta (ESP) at 
     * 1:1.</li>
     * <li>The Austrian schilling (ATS) was the official currency of Austria 
     * from 1925 to 2002 except during World War II and shortly before. The old 
     * notes and coins can be exchanged for euros indefinitely.</li>
     * <li>The Belgian franc (BEF) was the official currency of Belgium from 
     * 1832 to 2002. The old coins can no longer be exchanged for euros, but 
     * there's no time limit for exchanging the old notes.</li>
     * <li>The Cypriot pound (CYP) was a currency of Cyprus until 2008, when it 
     * was replaced by the euro. The old notes and coins can no longer be 
     * exchanged for euros.</li>
     * <li>The German mark (DEM) was the official currency of West Germany from 
     * 1948 to 1990 and of unified Germany from 1990 to 2002. The old notes and 
     * coins can be exchanged for euros indefinitely.</li>
     * <li>The Estonian kroon (EEK) was the official currency of Estonia from 
     * 1992 to 2011. The old notes and coins can be exchanged for euros at the 
     * country's central bank indefinitely.</li>
     * <li>The Spanish peseta (ESP) was the official currency of Spain from 1868 
     * to 2002. After June 30, 2021, the old notes and coins can no longer be 
     * exchanged for euros.</li>
     * <li>The Finnish markka (FIM) was the official currency of Finland from 
     * 1860 to 2002. The old notes and coins can no longer be exchanged for 
     * euros.</li>
     * <li>The French franc (FRF) was the official currency of France from 1960 
     * to 2002, though its history dates back to 1795 and earlier. The old notes 
     * and coins can no longer be exchanged for euros.</li>
     * <li>The Greek drachma (GRD) was valid until 2002, as was the case with 
     * the currencies of several other countries that joined the eurozone in 
     * 1999. The old notes and coins can no longer be exchanged for euros.</li>
     * <li>The Irish pound (IEP) was the official currency of Ireland  until 
     * 2002. Old Irish pound notes and coins can be exchanged for euros at the 
     * Central Bank of Ireland indefinitely.</li>
     * <li>The Italian lira (ITL) was the only official currency of Italy from 
     * 1861 to 1999. The old notes and coins can no longer be exchanged for 
     * euros.</li>
     * <li>The Luxembourgian franc (LUF) was the official currency of Luxembourg 
     * from 1854 to 2002 except during World War II. But even after the war, 
     * some other countries' currencies were accepted in Luxembourg. That 
     * nation's residents have been characterized as enthusiastic adopters of 
     * the euro, which was introduced in 1999. The old coins can no longer be 
     * exchanged for euros. However, the old notes can be exchanged 
     * indefinitely.</li>
     * <li>The Maltese lira (MTL) was valid until 2008. Malta joined the 
     * European Union in 2004, after most of the other nations of the union had 
     * phased out their own old currencies. So the phasing out period for the 
     * lira was pretty much the whole year 2008. The old notes and coins can no 
     * longer be exchanged for euros.</li>
     * <li>The Dutch guilder (NLG), also called the florin, was valid until 
     * 2002. The old coins can no longer be exchanged for euros. However, the 
     * old notes can exchanged until January 1, 2032, with some conditions.</li>
     * <li>The Portuguese escudo (PTE) was the official currency of Portugal 
     * from 1911 to 2002. The old notes and coins can no longer be exchanged for 
     * euros.</li>
     * <li>The Slovenian tolar (SIT) was the official currency of Slovenia from 
     * 1991 to 2007. The old coins can no longer be exchanged for euros, but the 
     * old notes can be exchanged for euros indefinitely.</li>
     * </ul>
     * <p>I have not yet verified this is the complete list of euro-replaced 
     * currencies.</p>
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
     * Chooses a currency according to a specified predicate.
     * @param predicate The predicate. For example, currency's display name 
     * should contain the word "dollar".
     * @return A currency satisfying the predicate. For the example predicate, 
     * for example, the Surinamese dollar (SRD). 
     */
    public static Currency chooseCurrency(Predicate<Currency> predicate) {
        boolean found = false;
        Currency currency = chooseCurrency();
        while (!found) {
            currency = chooseCurrency();
            found = predicate.test(currency);
        }
        return currency;
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
    
    /**
     * Chooses a pair of currencies other than a specified pair. 
     * @param pair A pair of currencies. For example, United States dollars 
     * (USD) to euros (EUR).
     * @return Another pair of currencies. Might have the same currencies but 
     * flipped (in the example, EUR to USD), or it might match the source 
     * currency and differ in the target currency, or it might match the target 
     * currency but differ in the source currency. We do guarantee that the pair 
     * will consist of two different currencies. That is, it won't be USD to 
     * USD, nor EUR to EUR, nor any other such pair. Example returns: Nepalese 
     * rupees (NPR) to Algerian dinars (DZD), East Caribbean dollars (XCD) to 
     * South Korean won (KRW) and Surinamese dollars (SRD) to Turkish lire 
     * (TRY).
     */
    public static CurrencyPair choosePairOtherThan(CurrencyPair pair) {
        CurrencyPair other = pair;
        while (pair.equals(other)) {
            Currency from = chooseCurrency();
            Currency to = chooseCurrencyOtherThan(from);
            other = new CurrencyPair(from, to);
        }
        return other;
    }

    // TODO: Write tests for this
    public static Set<CurrencyPair> choosePairs(int numberOfPairs) {
        return new HashSet<>();
    }

}
