/*
 * Copyright (C) 2025 Alonso del Arte
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

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.testframe.api.Asserters.assertContainsSame;
import static org.testframe.api.Asserters.assertMinimum;
import static org.testframe.api.Asserters.assertThrows;
import static org.testframe.api.Asserters.assertTimeout;

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
    
    private static final int TOTAL_NUMBER_OF_CURRENCIES;
    
    private static final int NUMBER_OF_CALLS_MULTIPLIER_FOR_EXCLUSION_SEARCH 
            = 4;
    
    private static final int NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH 
            = NUMBER_OF_CALLS_MULTIPLIER_FOR_EXCLUSION_SEARCH 
            * CURRENCIES.size();
    
    private static final String[] EURO_REPLACED_EXCLUSION_CODES = {"ADP", "ATS", 
        "BEF", "CYP", "DEM", "EEK", "ESP", "FIM", "FRF", "GRD", "IEP", "ITL", 
        "LUF", "MTL", "NLG", "PTE", "SIT"};
    
    private static final String[] OTHER_EXCLUSION_CODES = {"AYM", "BGL", "BOV", 
        "CHE", "CHW", "COU", "GWP", "MGF", "MXV", "SRG", "STN", "TPE", "USN", 
        "USS", "UYI", "VED", "ZWN"};
    
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
        TOTAL_NUMBER_OF_CURRENCIES = CURRENCIES.size();
    }
    
    private static boolean hasYearSpanIndicated(Currency currency) {
        String displayName = currency.getDisplayName();
        return displayName.contains("\u002818") 
                || displayName.contains("\u002819") 
                || displayName.contains("\u002820");
    }
    
    private static boolean isEuroReplacedCurrency(Currency currency) {
        String key = currency.getCurrencyCode();
        return Arrays.binarySearch(EURO_REPLACED_EXCLUSION_CODES, key) > -1;
    }

    private static boolean isHistoricalCurrency(Currency currency) {
        return hasYearSpanIndicated(currency) 
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
        System.out.println("getSuitableCurrencies");
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
    public void testIsHistoricalCurrency() {
        System.out.println("isHistoricalCurrency");
        for (Currency currency : CURRENCIES) {
            if (hasYearSpanIndicated(currency)) {
                String msg = "Currency " + currency.getDisplayName() + " (" 
                        + currency.getCurrencyCode() 
                        + ") should be considered historical";
                assert CurrencyChooser.isHistoricalCurrency(currency) : msg;
            }
        }
    }
    
    @Test
    public void testIsEuroReplacedCurrency() {
        System.out.println("isEuroReplacedCurrency");
        Set<Currency> currencies = Arrays.asList(EURO_REPLACED_EXCLUSION_CODES)
                .stream().map(currencyCode 
                        -> Currency.getInstance(currencyCode))
                .collect(Collectors.toSet());
        for (Currency currency : currencies) {
            String msg = "Currency " + currency.getDisplayName() + " (" 
                    + currency.getCurrencyCode() 
                    + ") should be considered a euro-replaced currency";
            assert CurrencyChooser.isEuroReplacedCurrency(currency) : msg;
        }
    }
    
    // TODO: Write @Test public void testEuroReplacedIsHistoricalCurrency() {}
    
    // TODO: Write @Test public void testIsNotHistoricalCurrency() {}
    
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
        int minimum = 11 * totalNumberOfCurrencies / 20;
        int actual = samples.size();
        String msg = "Trying to pick " + numberOfTries + " times from set of " 
                + totalNumberOfCurrencies + " gave " + actual 
                + " distinct, should've given more than " + minimum 
                + " distinct";
        assertMinimum(minimum, actual, msg);
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
        int minimum = 11 * numberOfTries / 20;
        int actual = samples.size();
        String msg = "Trying to pick " + numberOfTries + " other than " 
                + dollarsDisplayName + " gave " + actual 
                + " distinct, should've given at least " + minimum 
                + " distinct";
        assertMinimum(minimum, actual, msg);
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
        int minimum = 11 * numberOfTries / 20;
        int actual = samples.size();
        String msg = "Trying to pick " + numberOfTries + " other than " 
                + currencyDisplayName + " gave " + actual 
                + " distinct, should've given more than " + minimum 
                + " distinct";
        assertMinimum(minimum, actual, msg);
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
        Random random = new Random();
        int bound = 128;
        int unlikelyFractionDigits = bound + random.nextInt(bound);
        String msg = "Asking for currency with " + unlikelyFractionDigits 
                + " fraction digits should cause exception";
        Throwable t = assertThrows(() -> {
            Currency badCurrency 
                    = CurrencyChooser.chooseCurrency(unlikelyFractionDigits);
            System.out.println("Somehow asking for currency with " 
                    + unlikelyFractionDigits + " fraction digits gave " 
                    + badCurrency.getDisplayName() + " (" 
                    + badCurrency.getCurrencyCode() + "), which only has "
                    + badCurrency.getDefaultFractionDigits()
                    + " fraction digits");
        }, NoSuchElementException.class, msg); 
        String excMsg = t.getMessage();
        assert excMsg != null : "Message should not be null";
        System.out.println("\"" + excMsg + "\"");
        String digitString = Integer.toString(unlikelyFractionDigits);
        String containsMsg = "Exception message should include \"" + digitString 
                + "\"";
        assert excMsg.contains(digitString) : containsMsg;
    }
    
    @Test
    public void testChooseNoCentsCurrencyRandomlyEnough() {
        int fractionDigits = 0;
        Set<Currency> noCentCurrencies = FRACT_DIGITS_MAP.get(fractionDigits);
        int total = noCentCurrencies.size();
        Set<Currency> chosenCurrencies = new HashSet<>();
        for (int i = 0; i < total; i++) {
            Currency currency = CurrencyChooser.chooseCurrency(fractionDigits);
            String message = "Currency " + currency.getDisplayName() + " (" 
                    + currency.getCurrencyCode() + ") expected to have " 
                    + fractionDigits + " fraction digits";
            assertEquals(currency.getDefaultFractionDigits(), fractionDigits, 
                    message);
            chosenCurrencies.add(currency);
        }
        int minimum = total / 3;
        int actual = chosenCurrencies.size();
        String msg = "Out of " + total 
                + " currencies with no divisions, at least " + minimum 
                + " should've been chosen, " + actual + " were chosen";
        assertMinimum(minimum, actual, msg);
    }
    
    @Test
    public void testChooseCentCurrencyRandomlyEnough() {
        int fractionDigits = 2;
        Set<Currency> noCentCurrencies = FRACT_DIGITS_MAP.get(fractionDigits);
        int total = noCentCurrencies.size();
        Set<Currency> chosenCurrencies = new HashSet<>();
        int maxCallCount = total / 3;
        for (int i = 0; i < maxCallCount; i++) {
            Currency currency = CurrencyChooser.chooseCurrency(fractionDigits);
            String message = "Currency " + currency.getDisplayName() + " (" 
                    + currency.getCurrencyCode() + ") expected to have " 
                    + fractionDigits + " fraction digits";
            assertEquals(currency.getDefaultFractionDigits(), fractionDigits, 
                    message);
            chosenCurrencies.add(currency);
        }
        int minimum = maxCallCount / 8;
        int actual = chosenCurrencies.size();
        String msg = "Out of " + total 
                + " currencies dividing into 100 cents, at least " + minimum 
                + " should've been chosen after " + maxCallCount + " calls, " 
                + actual + " were chosen";
        assertMinimum(minimum, actual, msg);
    }
    
    @Test
    public void testChooseDarahimCurrencyRandomlyEnough() {
        int fractionDigits = 3;
        Set<Currency> noCentCurrencies = FRACT_DIGITS_MAP.get(fractionDigits);
        int total = noCentCurrencies.size();
        Set<Currency> chosenCurrencies = new HashSet<>();
        for (int i = 0; i < total; i++) {
            Currency currency = CurrencyChooser.chooseCurrency(fractionDigits);
            String message = "Currency " + currency.getDisplayName() + " (" 
                    + currency.getCurrencyCode() + ") expected to have " 
                    + fractionDigits + " fraction digits";
            assertEquals(currency.getDefaultFractionDigits(), fractionDigits, 
                    message);
            chosenCurrencies.add(currency);
        }
        int minimum = total / 2;
        int actual = chosenCurrencies.size();
        String msg = "Out of " + total 
                + " currencies dividing into 1,000 darahim, at least " 
                + minimum + " should've been chosen, " + actual 
                + " were chosen";
        assertMinimum(minimum, actual, msg);
    }
    
    @Test
    public void testChooseCurrencyByPredicate() {
        int remainder = ((int) System.currentTimeMillis()) % 16;
        Predicate<Currency> predicate 
                = (currency) -> currency.getNumericCode() % 16 == remainder;
        Set<Currency> filtered = CURRENCIES.stream().filter(predicate)
                .filter((currency) -> accept(currency))
                .collect(Collectors.toSet());
        Set<Currency> expected = new HashSet<>(filtered);
        Set<Currency> actual = new HashSet<>();
        String msg = "Choosing currencies with numeric code " + remainder 
                + " modulo 16";
        int totalNumberOfCalls = 20 * expected.size();
        int callsSoFar = 0;
        while (callsSoFar < totalNumberOfCalls) {
            actual.add(CurrencyChooser.chooseCurrency(predicate));
            callsSoFar++;
        }
        assertContainsSame(expected, actual, msg);
    }
    
    @Test
    public void testChooseCurrencyFromSetRejectsEmptySet() {
        Set<Currency> set = new HashSet<>();
        String msg = "Trying to choose from empty set should cause exception";
        Throwable t = assertThrows(() -> {
            Currency badChoice = CurrencyChooser.chooseCurrency(set);
            System.out.println(msg + ", not given result " 
                    + badChoice.getDisplayName() + " (" 
                    + badChoice.getCurrencyCode() + ")");
        }, NoSuchElementException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
    
    @Test
    public void testChooseCurrencyFromSet() {
        Random random = new Random();
        int initialCapacity = random.nextInt(16) + 4;
        Set<Currency> expected = new HashSet<>(initialCapacity);
        List<Currency> currencies = new ArrayList<>(CURRENCIES);
        while (expected.size() < initialCapacity) {
            expected.add(currencies.get(random
                    .nextInt(TOTAL_NUMBER_OF_CURRENCIES)));
        }
        Set<Currency> actual = new HashSet<>(initialCapacity);
        int numberOfCalls = 12 * initialCapacity;
        for (int i = 0; i < numberOfCalls; i++) {
            actual.add(CurrencyChooser.chooseCurrency(expected));
        }
        assertContainsSame(expected, actual);
    }
    
    @Test
    public void testChooseCurrencyByBadPredicateCausesException() {
        String invalidDisplayName = "Invalid display name " 
                + System.currentTimeMillis();
        Predicate<Currency> predicate 
                = (Currency cur) -> cur.getDisplayName()
                        .equals(invalidDisplayName);
        Duration allottedTime = Duration.of(10, ChronoUnit.SECONDS);
        String msg = "Bad predicate for invalid display name \"" 
                + invalidDisplayName + "\" should not take more than " 
                + allottedTime.toString() + " to cause exception";
        assertTimeout(() -> {
            Throwable t = assertThrows(() -> {
                Currency currency = CurrencyChooser.chooseCurrency(predicate);
                System.out.println("Search for \"" + invalidDisplayName 
                        + "\" somehow gave " + currency.getDisplayName() + " (" 
                        + currency.getCurrencyCode() + ")");
            }, NoSuchElementException.class);
            String excMsg = t.getMessage();
            assert excMsg != null : "Exception message should not be null";
            assert !excMsg.isBlank() : "Exception message should not be blank";
            System.out.println("\"" + excMsg + "\"");
        }, allottedTime, msg);
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
    public void testImproperAzerbaijanManatExcluded() {
        Currency improperManat = Currency.getInstance("AYM");
        String improperManatDisplayName = improperManat.getDisplayName();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be " + improperManatDisplayName;
            assertNotEquals(improperManat, currency, msg);
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
    public void testGuineaBissauPesoExcluded() {
        Currency pesos = Currency.getInstance("GWP");
        String pesosDisplayName = pesos.getDisplayName();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be " + pesosDisplayName;
            assertNotEquals(pesos, currency, msg);
        }
    }

    @Test
    public void testMalagasyFrancExcluded() {
        Currency francs = Currency.getInstance("MGF");
        String francsDisplayName = francs.getDisplayName();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be " + francsDisplayName;
            assertNotEquals(francs, currency, msg);
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
    public void testSurinameseGuilderExcluded() {
        Currency surinameseGuilder = Currency.getInstance("SRG");
        String surinameseGuilderDisplayName 
                = surinameseGuilder.getDisplayName();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be " + surinameseGuilderDisplayName;
            assertNotEquals(surinameseGuilder, currency, msg);
        }
    }
    
    @Test
    public void testSaoTomeAndPrincipeDobraExcluded() {
        Currency saoTomeAndPrincipeDobra = Currency.getInstance("STN");
        String saoTomeAndPrincipeDobraDisplayName 
                = saoTomeAndPrincipeDobra.getDisplayName();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be " + saoTomeAndPrincipeDobraDisplayName;
            assertNotEquals(saoTomeAndPrincipeDobra, currency, msg);
        }
    }

    @Test
    public void testTimoreseEscudoExcluded() {
        Currency escudos = Currency.getInstance("TPE");
        String unitsDisplayName = escudos.getDisplayName();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be " + unitsDisplayName;
            assertNotEquals(escudos, currency, msg);
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
    
    @Test
    public void testBolivarDigitalExcluded() {
        Currency bolivarDigital = Currency.getInstance("VED");
        String bolivarDigitalDisplayName = bolivarDigital.getDisplayName();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be " + bolivarDigitalDisplayName 
                    + " (the so-called bolivar dig\u00EDtal)";
            assertNotEquals(bolivarDigital, currency, msg);
        }
    }
    
    // TODO: After December 31, 2025, add "BGN" (for the Bulgarian lev) to 
    // EURO_REPLACED_EXCLUSION_CODES and delete this test, which for now is a 
    // reminder.
    @org.testng.annotations.Ignore
    @Test
    public void testBulgarianLevExcluded() {
        Currency bulgarianLev = Currency.getInstance("BGN");
        String bulgarianLevDisplayName = bulgarianLev.getDisplayName();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be " + bulgarianLevDisplayName;
            assertNotEquals(bulgarianLev, currency, msg);
        }
    }

    @Test
    public void testZimbabweanDollarExcluded() {
        Currency zimbabweanDollar = Currency.getInstance("ZWN");
        String zimbabweanDollarDisplayName = zimbabweanDollar.getDisplayName();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            Currency currency = CurrencyChooser.chooseCurrency();
            String msg = "Currency " + currency.getDisplayName() 
                    + " should not be " + zimbabweanDollarDisplayName;
            assertNotEquals(zimbabweanDollar, currency, msg);
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
    
    @Test
    public void testChoosePairOtherThan() {
        System.out.println("choosePairOtherThan");
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrency();
        CurrencyPair pair = new CurrencyPair(from, to);
        Set<CurrencyPair> pairs 
                = new HashSet<>(NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH);
        String msgPart = " should not be " + pair.toString();
        for (int i = 0; i < NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH; i++) {
            CurrencyPair other = CurrencyChooser.choosePairOtherThan(pair);
            String message = other.toString() + msgPart;
            assertNotEquals(pair, other, message);
            pairs.add(other);
        }
        int minimum = 3 * NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH / 5;
        int actual = pairs.size();
        String msg = "After " + NUMBER_OF_CALLS_FOR_EXCLUSION_SEARCH 
                + " calls, there should be at least " + minimum 
                + " distinct currency pairs";
        assertMinimum(minimum, actual, msg);
    }

    @Test
    public void testChoosePairsRejectsNegativeSize() {
        Random random = new Random();
        int badSize = -random.nextInt(Byte.MAX_VALUE) - 1;
        String msg = "choosePairs() should reject number of pairs "
                + badSize;
        Throwable t = assertThrows(() -> {
            Set<CurrencyPair> badResult = CurrencyChooser.choosePairs(badSize);
            System.out.println(msg + ", not given result " + badResult);
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        String numStr = Integer.toString(badSize);
        String containsMsg = "Exception message should contain \"" + numStr + "\"";
        assert excMsg.contains(numStr) : containsMsg;
        System.out.println("\"" + excMsg + "\"");
    }

}
