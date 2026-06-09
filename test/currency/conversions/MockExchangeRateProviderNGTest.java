/*
 * Copyright (C) 2026 Alonso del Arte
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
package currency.conversions;

import currency.CurrencyChooser;
import currency.CurrencyPair;
import static currency.conversions.ExchangeRateProviderNGTest.RANDOM;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.testframe.api.Asserters.assertContainsSame;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the MockExchangeRateProvider class.
 * @author Alonso del Arte
 */
public class MockExchangeRateProviderNGTest {
    
    private static final double TEST_DELTA = 0.00001;
    
    private static final Currency UNITED_STATES_DOLLARS 
            = Currency.getInstance(Locale.US);
    
    private static ConversionRateQuote[] inventQuotes() {
        int len = RANDOM.nextInt(16) + 4;
        ConversionRateQuote[] array = new ConversionRateQuote[len];
        LocalDateTime date = LocalDateTime.now();
        for (int i = 0; i < len; i++) {
            Currency to = CurrencyChooser
                    .chooseCurrencyOtherThan(UNITED_STATES_DOLLARS);
            double rate = 1.0 + RANDOM.nextDouble();
            CurrencyPair currencies = new CurrencyPair(UNITED_STATES_DOLLARS, 
                    to);
            date = date.minusDays(i);
            ConversionRateQuote quote = new ConversionRateQuote(currencies, 
                    rate, date);
            array[i] = quote;
        }
        return array;
    }
    
    @Test
    public void testGetRate() {
        System.out.println("getRate");
        ConversionRateQuote[] rateQuotes = inventQuotes();
        ExchangeRateProvider instance 
                = new MockExchangeRateProvider(rateQuotes);
        for (ConversionRateQuote quote : rateQuotes) {
            CurrencyPair pair = quote.getCurrencies();
            Currency source = pair.getFromCurrency();
            Currency target = pair.getToCurrency();
            double expected = quote.getRate();
            double actual = instance.getRate(source, target);
            String message = "Retrieving mock rate of conversion from " 
                    + source.getDisplayName() + " (" + source.getCurrencyCode() 
                    + ") to " + target.getDisplayName() + " (" 
                    + target.getCurrencyCode() + ")";
            assertEquals(actual, expected, TEST_DELTA, message);
        }
    }
    
    @Test
    public void testUnspecifiedPairGives1To1Rate() {
        ConversionRateQuote[] rateQuotes = inventQuotes();
        ExchangeRateProvider instance 
                = new MockExchangeRateProvider(rateQuotes);
        ConversionRateQuote quote 
                = rateQuotes[RANDOM.nextInt(rateQuotes.length)];
        CurrencyPair pair = quote.getCurrencies().flip();
            Currency source = pair.getFromCurrency();
            Currency target = pair.getToCurrency();
        double expected = 1.0;
        double actual = instance.getRate(source, target);
        String message = "Given that mock rate of conversion from " 
                + source.getDisplayName() + " (" + source.getCurrencyCode() 
                + ") to " + target.getDisplayName() + " (" 
                + target.getCurrencyCode() 
                + ") not specified, should give 1.0, not try to deduce";
        assertEquals(actual, expected, TEST_DELTA, message);
    }
    
    @Test
    public void testSupportedCurrencies() {
        System.out.println("supportedCurrencies");
        ConversionRateQuote[] rateQuotes = inventQuotes();
        ExchangeRateProvider instance 
                = new MockExchangeRateProvider(rateQuotes);
        Set<Currency> expected = new HashSet<>();
        for (ConversionRateQuote quote : rateQuotes) {
            CurrencyPair pair = quote.getCurrencies();
            expected.add(pair.getFromCurrency());
            expected.add(pair.getToCurrency());
        }
        Set<Currency> actual = instance.supportedCurrencies();
        String msg = "Currencies should be those in quotes array";
        assertContainsSame(expected, actual, msg);
    }
    
    @Test
    public void testSupportedCurrenciesDoesNotLeakField() {
        ConversionRateQuote[] rateQuotes = inventQuotes();
        ExchangeRateProvider instance 
                = new MockExchangeRateProvider(rateQuotes);
        Set<Currency> quotedCurrencies = instance.supportedCurrencies();
        Set<Currency> expected = new HashSet<>(quotedCurrencies);
        quotedCurrencies.clear();
        Set<Currency> actual = instance.supportedCurrencies();
        assertEquals(actual, expected);
    }
    
}
