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

import static currency.CurrencyChooser.RANDOM;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Locale;

import static org.testframe.api.Asserters.assertInRange;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the MockExchangeRateProvider class.
 * @author Alonso del Arte
 */
public class MockExchangeRateProviderNGTest {
    
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
        double delta = 0.00001;
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
            assertEquals(actual, expected, delta, message);
        }
    }
    
}
