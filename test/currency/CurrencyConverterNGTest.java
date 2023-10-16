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

import java.util.Currency;
import java.util.Locale;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyConverter class.
 * @author Alonso del Arte
 */
public class CurrencyConverterNGTest {
    
    private static final double TEST_DELTA = 0.0001;
    
    private static final Currency U_S_DOLLARS = Currency.getInstance(Locale.US);
    
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
    
    /*
     * Test of the getRate function, of the CurrencyConverter class.
     */
//    @Test
//    public void testGetRate() {
//        System.out.println("getRate");
//        fail("The test case is a prototype.");
//        Currency source = null;
//        Currency target = null;
//        String expResult = "";
//        String result = CurrencyConverter.getRate(source, target);
//        assertEquals(result, expResult);
//        // TODO review the generated test code and remove the default call to fail.
//    }
    
}
