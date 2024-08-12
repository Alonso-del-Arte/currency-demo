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

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyPair class.
 * @author Alonso del Arte
 */
public class CurrencyPairNGTest {
    
    /**
     * Test of getFromCurrency method, of class CurrencyPair.
     */
    @Test
    public void testGetFromCurrency() {
        System.out.println("getFromCurrency");
        CurrencyPair instance = null;
        Currency expResult = null;
        Currency result = instance.getFromCurrency();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getToCurrency method, of class CurrencyPair.
     */
    @Test
    public void testGetToCurrency() {
        System.out.println("getToCurrency");
        CurrencyPair instance = null;
        Currency expResult = null;
        Currency result = instance.getToCurrency();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
