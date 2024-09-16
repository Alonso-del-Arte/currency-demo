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

import java.util.Currency;
import java.util.Locale;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the MannysCurrencyConverterAPIAccess class.
 * @author Alonso del Arte
 */
public class MannysCurrencyConverterAPIAccessNGTest {
    
    private static final double TEST_DELTA = 0.001;
    
    private static final Currency U_S_DOLLARS = Currency.getInstance(Locale.US);
    
    private static final Currency EAST_CARIBBEAN_DOLLARS 
            = Currency.getInstance("XCD");
    
    @Test
    public void testGetRateNoConversionNeeded() {
        ExchangeRateProvider instance = new MannysCurrencyConverterAPIAccess();
        Currency currency = CurrencyChooser.chooseCurrency();
        double expected = 1.0;
        double actual = instance.getRate(currency, currency);
        String iso4217Code = currency.getCurrencyCode();
        String message = "No conversion needed for " + iso4217Code + " to " 
                + iso4217Code;
        assertEquals(actual, expected, TEST_DELTA, message);
    }
    
    @Test
    public void testGetRateForUSDollarsToEastCaribbeanDollars() {
        ExchangeRateProvider instance = new MannysCurrencyConverterAPIAccess();
        double expected = 2.702;
        double actual = instance.getRate(U_S_DOLLARS, EAST_CARIBBEAN_DOLLARS);
        assertEquals(actual, expected, TEST_DELTA);
    }
    
    @Test
    public void testGetRateForEastCaribbeanDollarsToUSDollars() {
        ExchangeRateProvider instance = new MannysCurrencyConverterAPIAccess();
        double expected = 0.37;
        double actual = instance.getRate(EAST_CARIBBEAN_DOLLARS, U_S_DOLLARS);
        assertEquals(actual, expected, TEST_DELTA);
    }
    
    /**
     * Test of the getRate function, of the MannysCurrencyConverterAPIAccess 
     * class.
     */
    @Test
    public void testGetRate() {
        System.out.println("getRate");
        ExchangeRateProvider instance = new MannysCurrencyConverterAPIAccess();
        Currency firstTarget = CurrencyChooser
                .chooseCurrencyOtherThan(U_S_DOLLARS);
        String dollarsDisplayName = U_S_DOLLARS.getDisplayName();
        String dollarsISO4217Code = U_S_DOLLARS.getCurrencyCode();
        String firstTargetDisplayName = firstTarget.getDisplayName();
        String firstTargetISO4217Code = firstTarget.getCurrencyCode();
        System.out.println("Inquiring rate of conversion from " 
                + dollarsDisplayName + " (" + dollarsISO4217Code + ") to " 
                + firstTargetDisplayName + " (" + firstTargetISO4217Code + ")");
        double fromDollars = instance.getRate(U_S_DOLLARS, firstTarget);
        double toDollars = instance.getRate(firstTarget, U_S_DOLLARS);
        double expected = 1.0;
        double actual = fromDollars * toDollars;
        String message = "Rate of conversion from " + dollarsDisplayName + " (" 
                + dollarsISO4217Code + ") to " + firstTargetDisplayName + " (" 
                + firstTargetISO4217Code + ") is said to be " + fromDollars 
                + ", and vice-versa is said to be " + toDollars;
        System.out.println(message);
        assertEquals(actual, expected, TEST_DELTA, message);
    }

}
