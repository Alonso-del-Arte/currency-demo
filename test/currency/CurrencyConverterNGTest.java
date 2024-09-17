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

import java.util.Currency;
import java.util.Locale;

import static org.testframe.api.Asserters.assertInRange;
import static org.testframe.api.Asserters.assertThrows;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyConverter class. The original idea limited the class 
 * under test to a single online API. In the changeover away from static 
 * functions, this test class should test that instances use the given exchange 
 * rate provider, not that the conversions are real or even plausible.
 * @author Alonso del Arte
 */
public class CurrencyConverterNGTest {
    
    private static final double TEST_DELTA = 0.001;
    
    private static final Currency U_S_DOLLARS = Currency.getInstance(Locale.US);
    
    private static final Currency EAST_CARIBBEAN_DOLLARS 
            = Currency.getInstance("XCD");
    
    @Test
    public void testGetProvider() {
        System.out.println("getProvider");
        ExchangeRateProvider expected = new MockExchangeRateProvider();
        CurrencyConverter instance = new CurrencyConverter(expected);
        ExchangeRateProvider actual = instance.getProvider();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testConvert() {
        System.out.println("convert");
        Currency from = CurrencyChooser.chooseCurrency(0);
        Currency target = CurrencyChooser.chooseCurrency(3);
        CurrencyPair currencies = new CurrencyPair(from, target);
        double rate = 1.0 + (RANDOM.nextDouble() / 100);
        ConversionRateQuote quote = new ConversionRateQuote(currencies, rate);
        ExchangeRateProvider rateProvider = new MockExchangeRateProvider(quote);
        CurrencyConverter instance = new CurrencyConverter(rateProvider);
        int units = RANDOM.nextInt(16384) + 16;
        MoneyAmount source = new MoneyAmount(units, from);
        double intermediate = rate * units;
        double floored = Math.floor(intermediate);
        int expUnits = (int) floored;
        double roughDivs = intermediate - floored;
        short divisions = (short) Math.floor(roughDivs * 1000);
        MoneyAmount expected = new MoneyAmount(expUnits, target, divisions);
        MoneyAmount oneUnit = new MoneyAmount(1, target);
        MoneyAmount minimum = expected.minus(oneUnit);
        MoneyAmount maximum = expected.plus(oneUnit);
        MoneyAmount actual = instance.convert(source, target);
        String msg = "Given mock rate of " + rate + " to convert from " 
                + from.getDisplayName() + " (" + from.getCurrencyCode() 
                + ") to " + target.getDisplayName() + " (" 
                + target.getCurrencyCode() + ") expected " + source.toString() 
                + " to convert to roughly " + expected.toString();
        assertInRange(minimum, actual, maximum, msg);
    }
    
    @Test
    public void testConstructorRejectsNullProvider() {
        String msg = "Trying to use null provider should cause exception";
        Throwable t = assertThrows(() -> {
            CurrencyConverter badConverter = new CurrencyConverter(null);
            System.out.println(msg + ", not given instance " 
                    + badConverter.toString());
        }, NullPointerException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
    
}
