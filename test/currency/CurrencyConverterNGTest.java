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

import static currency.CurrencyChooser.RANDOM;

import java.util.Currency;
import java.util.Locale;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyConverter class.
 * @author Alonso del Arte
 */
public class CurrencyConverterNGTest {
    
    private static final double TEST_DELTA = 0.001;
    
    private static final Currency U_S_DOLLARS = Currency.getInstance(Locale.US);
    
    private static final Currency EAST_CARIBBEAN_DOLLARS 
            = Currency.getInstance("XCD");
    
    @Test
    public void testGetRateNoConversionNeeded() {
        Currency currency = CurrencyChooser.chooseCurrency();
        double expected = 1.0;
        double actual = Double.parseDouble(CurrencyConverter.getRate(currency, 
                currency));
        String iso4217Code = currency.getCurrencyCode();
        String msg = "No conversion needed for " + iso4217Code + " to " 
                + iso4217Code;
        assertEquals(actual, expected, TEST_DELTA, msg);
    }
    
    @Test
    public void testGetRateForUSDollarsToEastCaribbeanDollars() {
        double expected = 2.702;
        double actual = Double.parseDouble(CurrencyConverter
                .getRate(U_S_DOLLARS, EAST_CARIBBEAN_DOLLARS));
        assertEquals(actual, expected, TEST_DELTA);
    }
    
    @Test
    public void testGetRateForEastCaribbeanDollarsToUSDollars() {
        double expected = 1.0 / 2.7;
        double actual = Double.parseDouble(CurrencyConverter
                .getRate(EAST_CARIBBEAN_DOLLARS, U_S_DOLLARS));
        assertEquals(actual, expected, TEST_DELTA);
    }
    
    /**
     * Test of the getRate function, of the CurrencyConverter class.
     */
    @Test
    public void testGetRate() {
        System.out.println("getRate");
        Currency firstTarget = CurrencyChooser
                .chooseCurrencyOtherThan(U_S_DOLLARS);
        String dollarsDisplayName = U_S_DOLLARS.getDisplayName();
        String dollarsISO4217Code = U_S_DOLLARS.getCurrencyCode();
        String firstTargetDisplayName = firstTarget.getDisplayName();
        String firstTargetISO4217Code = firstTarget.getCurrencyCode();
        System.out.println("Inquiring rate of conversion from " 
                + dollarsDisplayName + " (" + dollarsISO4217Code + ") to " 
                + firstTargetDisplayName + " (" + firstTargetISO4217Code + ")");
        double fromDollars = Double.parseDouble(CurrencyConverter
                .getRate(U_S_DOLLARS, firstTarget));
        double toDollars = Double.parseDouble(CurrencyConverter
                .getRate(firstTarget, U_S_DOLLARS));
        double expected = 1.0;
        double actual = fromDollars * toDollars;
        String message = "Rate of conversion from " + dollarsDisplayName + " (" 
                + dollarsISO4217Code + ") to " + firstTargetDisplayName + " (" 
                + firstTargetISO4217Code + ") is said to be " + fromDollars 
                + ", and vice-versa is said to be " + toDollars;
        System.out.println(message);
        assertEquals(actual, expected, TEST_DELTA, message);
    }
    
    @Test
    public void testConvertButNoConvertNeeded() {
        int units = RANDOM.nextInt(Short.MAX_VALUE);
        Currency currency = CurrencyChooser.chooseCurrency();
        MoneyAmount expected = new MoneyAmount(units, currency);
        MoneyAmount actual = CurrencyConverter.convert(expected, currency);
        String message = "Converting " + expected.toString() + " to " 
                + currency.getDisplayName() + " should not need conversion";
        assertEquals(actual, expected, message);
    }
    
}
