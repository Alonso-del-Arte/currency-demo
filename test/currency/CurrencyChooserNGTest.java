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

import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.testframe.api.Asserters.assertContainsSame;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyChooser class.
 * @author Alonso del Arte
 */
public class CurrencyChooserNGTest {
    
    private static final Set<Currency> CURRENCIES 
            = Currency.getAvailableCurrencies();
    
    private static final Set<Currency> PSEUDO_CURRENCIES = new HashSet<>();
    
    private static final Map<Integer, Set<Currency>> FRACT_DIGITS_MAP 
            = new HashMap<>();
    
    private static final Currency DOLLARS = Currency.getInstance(Locale.US);
    
    private static final int TOTAL_NUMBER_OF_CURRENCIES = CURRENCIES.size();
    
    private static final int NUMBER_OF_CALLS_MULTIPLIER_FOR_EXCLUSION_SEARCH 
            = 4;
    
    private static final int NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH 
            = NUMBER_OF_CALLS_MULTIPLIER_FOR_EXCLUSION_SEARCH 
            * TOTAL_NUMBER_OF_CURRENCIES;
    
    private static final String[] EURO_REPLACED_EXCLUSION_CODES = {"ADP", "EEK", 
        "FIM", "GRD", "ITL", "MTL", "NLG", "PTE", "SIT"};
    
    private static final String[] OTHER_EXCLUSION_CODES = {"BGL", "BOV", "CHE", 
        "CHW", "COU", "MXV", "USN", "USS", "UYI"};
    
    static {
        for (Currency currency : CURRENCIES) {
            int fractDigits = currency.getDefaultFractionDigits();
            if (fractDigits < 0) {
                PSEUDO_CURRENCIES.add(currency);
            } else {
                Set<Currency> digitGroupedSet;
                if (FRACT_DIGITS_MAP.containsKey(fractDigits)) {
                    digitGroupedSet = FRACT_DIGITS_MAP.get(fractDigits);
                } else {
                    digitGroupedSet = new HashSet<>();
                    FRACT_DIGITS_MAP.put(fractDigits, digitGroupedSet);
                }
                digitGroupedSet.add(currency);
            }
        }
        CURRENCIES.removeAll(PSEUDO_CURRENCIES);
    }
    
    private static boolean isEuroReplacedCurrency(Currency currency) {
        String key = currency.getCurrencyCode();
        return Arrays.binarySearch(EURO_REPLACED_EXCLUSION_CODES, key) > -1;
    }

    private static boolean isHistoricalCurrency(Currency currency) {
        String displayName = currency.getDisplayName();
        return displayName.contains("\u002818") 
                || displayName.contains("\u002819") 
                || displayName.contains("\u002820") 
                || isEuroReplacedCurrency(currency);
    }
    
    private static boolean isPseudoCurrency(Currency currency) {
        return currency.getDefaultFractionDigits() < 0;
    } 
    
    private static boolean shouldOtherwiseBeExcluded(Currency currency) {
        String key = currency.getCurrencyCode();
        return Arrays.binarySearch(OTHER_EXCLUSION_CODES, key) > -1;
    } 
    
    private static boolean accept(Currency currency) {
        return !isHistoricalCurrency(currency) && !isPseudoCurrency(currency) 
                && !shouldOtherwiseBeExcluded(currency);
    } 
    
    @Test
    public void testGetSuitableCurrencies() {
        Set<Currency> currencies = Currency.getAvailableCurrencies();
        Set<Currency> expected = currencies.stream()
                .filter(currency -> accept(currency))
                .collect(Collectors.toSet());
        Set<Currency> actual = CurrencyChooser.getSuitableCurrencies();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testIsSuitableCurrency() {
        System.out.println("isSuitableCurrency");
        Set<Currency> currencies = CurrencyChooser.getSuitableCurrencies();
        for (Currency currency : currencies) {
            String msg = "Currency " + currency.getDisplayName() + " (" 
                    + currency.getCurrencyCode() 
                    + ") should be considered suitable";
            assert CurrencyChooser.isSuitableCurrency(currency) : msg;
        }
    }
    
    @Test
    public void testIsNotSuitableCurrency() {
        Set<Currency> complement = new HashSet<>(CURRENCIES);
        Set<Currency> suitables = CurrencyChooser.getSuitableCurrencies();
        complement.removeAll(suitables);
        for (Currency currency : complement) {
            String msg = "Currency " + currency.getDisplayName() + " (" 
                    + currency.getCurrencyCode() 
                    + ") should not be considered suitable";
            assert !CurrencyChooser.isSuitableCurrency(currency) : msg;
        }
    }
    
    @Test
    public void testChoosePseudocurrency() {
        System.out.println("choosePseudocurrency");
        int initialCapacity = PSEUDO_CURRENCIES.size();
        int numberOfCalls = initialCapacity * 10;
        Set<Currency> actual = new HashSet<>(initialCapacity);
        for (int i = 0; i < numberOfCalls; i++) {
            Currency pseudocurrency = CurrencyChooser.choosePseudocurrency();
            actual.add(pseudocurrency);
        }
        assertEquals(actual, PSEUDO_CURRENCIES);
    }
    
    @Test
    public void testChooseCurrency() {
        System.out.println("chooseCurrency");
        int totalNumberOfCurrencies = CURRENCIES.size();
        int numberOfTries = 5 * totalNumberOfCurrencies / 3;
        Set<Currency> samples = new HashSet<>();
        int sampleNumber = 0;
        while (sampleNumber < numberOfTries) {
            Currency sample = CurrencyChooser.chooseCurrency();
            String msg = "Chosen currency " + sample.getDisplayName() 
                    + " expected to not have negative fraction digits";
            assert sample.getDefaultFractionDigits() > -1 : msg;
            samples.add(sample);
            sampleNumber++;
        }
        int expected = 11 * totalNumberOfCurrencies / 20;
        int actual = samples.size();
        String msg = "Trying to pick " + numberOfTries + " times from set of " 
                + totalNumberOfCurrencies + " gave " + actual 
                + " distinct, should've given more than " + expected 
                + " distinct";
        assert expected <= actual : msg;
    }

    @Test
    public void testChooseCurrencyOtherThanDollars() {
        int numberOfTries = 40;
        Set<Currency> samples = new HashSet<>();
        int sampleNumber = 0;
        String dollarsDisplayName = DOLLARS.getDisplayName();
        while (sampleNumber < numberOfTries) {
            Currency sample = CurrencyChooser.chooseCurrencyOtherThan(DOLLARS);
            String msg = "Chosen currency " + sample.getDisplayName() 
                    + " expected to not be " + dollarsDisplayName;
            assert sample != DOLLARS : msg;
            samples.add(sample);
            sampleNumber++;
        }
        int expected = 11 * numberOfTries / 20;
        int actual = samples.size();
        String msg = "Trying to pick " + numberOfTries + " other than " 
                + dollarsDisplayName + " gave " + actual 
                + " distinct, should've given at least " + expected 
                + " distinct";
        assert expected <= actual : msg;
    }
    
    @Test
    public void testChooseCurrencyOtherThan() {
        System.out.println("chooseCurrencyOtherThan");
        Currency someCurrency 
                = CurrencyChooser.chooseCurrencyOtherThan(DOLLARS);
        int numberOfTries = 40;
        Set<Currency> samples = new HashSet<>();
        int sampleNumber = 0;
        String currencyDisplayName = someCurrency.getDisplayName();
        while (sampleNumber < numberOfTries) {
            Currency sample 
                    = CurrencyChooser.chooseCurrencyOtherThan(someCurrency);
            String msg = "Chosen currency " + sample.getDisplayName() 
                    + " expected to not be " + currencyDisplayName;
            assert sample != someCurrency : msg;
            samples.add(sample);
            sampleNumber++;
        }
        int expected = 11 * numberOfTries / 20;
        int actual = samples.size();
        String msg = "Trying to pick " + numberOfTries + " other than " 
                + currencyDisplayName + " gave " + actual 
                + " distinct, should've given more than " + expected 
                + " distinct";
        assert expected < actual : msg;
    }
    
    @Test
    public void testChooseCurrencyWithNoCentsOrDarahim() {
        int expected = 0;
        Currency currency = CurrencyChooser.chooseCurrency(expected);
        int actual = currency.getDefaultFractionDigits();
        String message = "Chosen currency " + currency.getDisplayName() 
                + " should have " + expected + " default fraction digits";
        assertEquals(actual, expected, message);
    }

    @Test
    public void testChooseCurrencyWith100Cents() {
        int expected = 2;
        Currency currency = CurrencyChooser.chooseCurrency(expected);
        int actual = currency.getDefaultFractionDigits();
        String message = "Chosen currency " + currency.getDisplayName() 
                + " should have " + expected + " default fraction digits";
        assertEquals(actual, expected, message);
    }

    @Test
    public void testChooseCurrencyWith1000Darahim() {
        int expected = 3;
        Currency currency = CurrencyChooser.chooseCurrency(expected);
        int actual = currency.getDefaultFractionDigits();
        String message = "Chosen currency " + currency.getDisplayName() 
                + " should have " + expected + " default fraction digits";
        assertEquals(actual, expected, message);
    }

    /**
     * Test of the chooseCurrency function of the CurrencyChooser class. If the 
     * requested number of fraction digits is 4, there might be only one answer 
     * the function can give: the Chilean unit of account (UF), ISO-4217 code 
     * CLF. One UF subdivides into what I'm going to call cents of cents until I 
     * can find a better term. It's actually kind of odd that the Chilean peso 
     * (CLP) no longer divides into cents like it used to. But that's a whole 
     * other topic.
     * <p>If your Java runtime does not recognize the Chilean UF nor any other 
     * currency with ten thousand divisions, maybe either ignore or comment this 
     * test out.</p>
     */
    @Test
    public void testChooseCurrencyWith10000Divisions() {
        int expected = 4;
        Currency currency = CurrencyChooser.chooseCurrency(expected);
        int actual = currency.getDefaultFractionDigits();
        String message = "Chosen currency " + currency.getDisplayName() 
                + " should have " + expected + " default fraction digits";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testUnavailableFractionDigitsCauseException() {
        int unlikelyFractionDigits = Integer.MAX_VALUE;
        try {
            Currency badCurrency 
                    = CurrencyChooser.chooseCurrency(unlikelyFractionDigits);
            System.out.println("Somehow asking for currency with " 
                    + unlikelyFractionDigits + " fraction digits gave " 
                    + badCurrency.getDisplayName());
        } catch (NoSuchElementException nsee) {
            String excMsg = nsee.getMessage();
            assert excMsg != null : "Message should not be null";
            System.out.println("\"" + excMsg + "\"");
            String digitString = Integer.toString(unlikelyFractionDigits);
            String msg = "Exception message should include \"" + digitString 
                    + "\"";
            assert excMsg.contains(digitString) : msg;
        } catch (RuntimeException re) {
            String message = re.getClass().getName() 
                    + " is the wrong exception to throw for " 
                    + unlikelyFractionDigits + " fraction digits";
            fail(message);
        }
    }
    
    @Test
    public void testChooseNoCentsCurrencyRandomlyEnough() {
        Set<Currency> noCentCurrencies = FRACT_DIGITS_MAP.get(0);
        int total = noCentCurrencies.size();
        Set<Currency> chosenCurrencies = new HashSet<>();
        for (int i = 0; i < total; i++) {
            chosenCurrencies.add(CurrencyChooser.chooseCurrency(0));
        }
        int expected = total / 3;
        int actual = chosenCurrencies.size();
        String msg = "Out of " + total 
                + " currencies with no divisions, at least " + expected 
                + " should've been chosen, " + actual + " were chosen";
        assert actual >= expected : msg;
    }
    
    @Test
    public void testChooseCentCurrencyRandomlyEnough() {
        Set<Currency> noCentCurrencies = FRACT_DIGITS_MAP.get(2);
        int total = noCentCurrencies.size();
        Set<Currency> chosenCurrencies = new HashSet<>();
        int maxCallCount = total / 3;
        int expected = maxCallCount / 8;
        for (int i = 0; i < maxCallCount; i++) {
            chosenCurrencies.add(CurrencyChooser.chooseCurrency(3));
        }
        int actual = chosenCurrencies.size();
        String msg = "Out of " + total 
                + " currencies dividing into 100 cents, at least " + expected 
                + " should've been chosen after " + maxCallCount + " calls, " 
                + actual + " were chosen";
        assert actual >= expected : msg;
    }
    
    @Test
    public void testChooseDarahimCurrencyRandomlyEnough() {
        Set<Currency> noCentCurrencies = FRACT_DIGITS_MAP.get(3);
        int total = noCentCurrencies.size();
        Set<Currency> chosenCurrencies = new HashSet<>();
        for (int i = 0; i < total; i++) {
            chosenCurrencies.add(CurrencyChooser.chooseCurrency(3));
        }
        int expected = total / 2;
        int actual = chosenCurrencies.size();
        String msg = "Out of " + total 
                + " currencies dividing into 1,000 darahim, at least " 
                + expected + " should've been chosen, " + actual 
                + " were chosen";
        assert actual >= expected : msg;
    }
    
    @Test
    public void testChooseCurrencyByPredicate() {
        int remainder = ((int) System.currentTimeMillis()) % 16;
        Predicate<Currency> predicate 
                = (currency) -> currency.getNumericCode() % 16 == remainder;
        // TODO: Refactor to use assertContainsSame()
        Set<Currency> filtered = CURRENCIES.stream().filter(predicate)
                .collect(Collectors.toSet());
        Set<Currency> expected = new HashSet<>(filtered);
        Set<Currency> actual = new HashSet<>();
        String message = "Choosing currencies with numeric code " + remainder 
                + " modulo 16";
        int totalNumberOfCalls = 20 * expected.size();
        int callsSoFar = 0;
        while (callsSoFar < totalNumberOfCalls) {
            actual.add(CurrencyChooser.chooseCurrency(predicate));
            callsSoFar++;
        }
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testHistoricalCurrenciesExcluded() {
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be a historical currency";
            assert !isHistoricalCurrency(currency) : msg;
        }
    }

    @Test
    public void testHistoricalCurrenciesExcludedFromFractDigitSpecs() {
        for (int places = 2; places < 5; places++) {
            for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
                Currency currency = CurrencyChooser.chooseCurrency(places);
                String msg = "Currency " + currency.getDisplayName() + " (" 
                        + currency.getCurrencyCode() + ") with " + places 
                        + " places should not be a historical currency";
                assert !isHistoricalCurrency(currency) : msg;
            }
        }
    }

    @Test
    public void testSameDayUSDollarExcluded() {
        Currency sameDayDollar = Currency.getInstance("USS");
        String sameDayDollarDisplayName = sameDayDollar.getDisplayName();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be " + sameDayDollarDisplayName;
            assertNotEquals(sameDayDollar, currency, msg);
        }
    }

    @Test
    public void testNextDayUSDollarExcluded() {
        Currency nextDayDollar = Currency.getInstance("USN");
        String nextDayDollarDisplayName = nextDayDollar.getDisplayName();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be " + nextDayDollarDisplayName;
            assertNotEquals(nextDayDollar, currency, msg);
        }
    }

    @Test
    public void testBulgarianHardLevExcluded() {
        Currency bulgarianHardLev = Currency.getInstance("BGL");
        String bulgarianHardLevDisplayName = bulgarianHardLev.getDisplayName();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be " + bulgarianHardLevDisplayName;
            assertNotEquals(bulgarianHardLev, currency, msg);
        }
    }

    @Test
    public void testWIREuroExcluded() {
        Currency wirEuro = Currency.getInstance("CHE");
        String wirEuroDisplayName = wirEuro.getDisplayName();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be " + wirEuroDisplayName;
            assertNotEquals(wirEuro, currency, msg);
        }
    }

    @Test
    public void testWIRFrancExcluded() {
        Currency wirFranc = Currency.getInstance("CHW");
        String wirFrancDisplayName = wirFranc.getDisplayName();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be " + wirFrancDisplayName;
            assertNotEquals(wirFranc, currency, msg);
        }
    }

    @Test
    public void testBolivianMVDOLExcluded() {
        Currency mvDol = Currency.getInstance("BOV");
        String mvDolDisplayName = mvDol.getDisplayName();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be " + mvDolDisplayName;
            assertNotEquals(mvDol, currency, msg);
        }
    }

    @Test
    public void testColombianRealValueUnitsExcluded() {
        Currency unitsRealValue = Currency.getInstance("COU");
        String unitsDisplayName = unitsRealValue.getDisplayName();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be " + unitsDisplayName;
            assertNotEquals(unitsRealValue, currency, msg);
        }
    }

    @Test
    public void testMexicanInvestmentUnitsExcluded() {
        Currency unitsInvestment = Currency.getInstance("MXV");
        String unitsDisplayName = unitsInvestment.getDisplayName();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be " + unitsDisplayName;
            assertNotEquals(unitsInvestment, currency, msg);
        }
    }

    @Test
    public void testUruguayanPesoIndexedUnitsExcluded() {
        Currency uyiIndexedUnits = Currency.getInstance("UYI");
        String uyiUIDisplayName = uyiIndexedUnits.getDisplayName();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be " + uyiUIDisplayName;
            assertNotEquals(uyiIndexedUnits, currency, msg);
        }
    }

    /**
     * Another test of the chooseCurrency function, of the CurrencyChooser 
     * class. The nations of the European Union had their own currencies prior 
     * to joining. In many cases, the euro (EUR) was introduced in 1999 but the 
     * nation's own currency was still valid until 2002. In the following list, 
     * the last date of validity is given only if it's after 2002.
     * <ul>
     * <li>Andorran peseta (ADP)</li>
     * <li>Estonian kroon (EEK)</li>
     * <li>Finnish markka (FIM)</li>
     * <li>Greek drachma (GRD)</li>
     * <li>Italian lira (ITL)</li>
     * <li>Maltese lira (MTL), it was valid until 2008</li>
     * <li>Dutch guilder (NLG), also called florin</li>
     * <li>Portuguese escudo (PTE)</li>
     * <li>Slovenian tolar (SIT)</li>
     * </ul>
     * <p>These currencies are not listed with any range of years in the 
     * currencies info file like other historical currencies.</p>
     */
    @Test
    public void testExcludeEuropeanCurrenciesReplacedByEuro() {
        for (String currencyCode : EURO_REPLACED_EXCLUSION_CODES) {
            Currency excludedCurrency = Currency.getInstance(currencyCode);
            String exclCurrDisplayName = excludedCurrency.getDisplayName();
            for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
                Currency currency = CurrencyChooser.chooseCurrency();
                String msg = "Currency " + currency.getDisplayName() 
                        + " should not be " + exclCurrDisplayName;
                assertNotEquals(excludedCurrency, currency, msg);
            }
        }
    }

}
