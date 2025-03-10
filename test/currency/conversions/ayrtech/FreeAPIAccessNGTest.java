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
package currency.conversions.ayrtech;

import java.util.Currency;
import java.util.Set;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the FreeAPIAccess class. This API requires an API key.
 * @author Alonso del Arte
 */
public class FreeAPIAccessNGTest {
    
    /**
     * Test of supportedCurrencies method, of class FreeAPIAccess.
     */
    @Test
    public void testSupportedCurrencies() {
        System.out.println("supportedCurrencies");
        FreeAPIAccess instance = new FreeAPIAccess();
        Set expResult = null;
        Set result = instance.supportedCurrencies();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRate method, of class FreeAPIAccess.
     */
    @Test
    public void testGetRate() {
        System.out.println("getRate");
        Currency source = null;
        Currency target = null;
        FreeAPIAccess instance = new FreeAPIAccess();
        double expResult = 0.0;
        double result = instance.getRate(source, target);
        assertEquals(result, expResult, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
