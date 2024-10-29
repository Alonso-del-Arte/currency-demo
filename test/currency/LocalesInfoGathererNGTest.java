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
import java.util.Map;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the LocalesInfoGatherer class.
 * @author Alonso del Arte
 */
public class LocalesInfoGathererNGTest {
    
    /**
     * Test of the getCurrency function, of the LocalesInfoGatherer class.
     */
    @Test
    public void testGetCurrency() {
        System.out.println("getCurrency");
        Currency expected = CurrencyChooser.chooseCurrency();
        LocalesInfoGatherer instance = new LocalesInfoGatherer(expected);
        Currency actual = instance.getCurrency();
        assertEquals(actual, expected);
    }

    /**
     * Test of getSymbols method, of class LocalesInfoGatherer.
     */
    @Test
    public void testGetSymbols() {
        System.out.println("getSymbols");
//        LocalesInfoGatherer instance = null;
//        Map expResult = null;
//        Map result = instance.getSymbols();
//        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDisplayNames method, of class LocalesInfoGatherer.
     */
    @Test
    public void testGetDisplayNames() {
        System.out.println("getDisplayNames");
//        LocalesInfoGatherer instance = null;
//        Map expResult = null;
//        Map result = instance.getDisplayNames();
//        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
