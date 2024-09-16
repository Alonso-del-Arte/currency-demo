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
     * Test of getRate method, of class MannysCurrencyConverterAPIAccess.
     */
//    @Test
    public void testGetRate() {
        System.out.println("getRate");
        Currency source = null;
        Currency target = null;
        MannysCurrencyConverterAPIAccess instance = new MannysCurrencyConverterAPIAccess();
        double expResult = 0.0;
        double result = instance.getRate(source, target);
        assertEquals(result, expResult, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
