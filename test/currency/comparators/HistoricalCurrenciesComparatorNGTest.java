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
package currency.comparators;

import java.time.Year;
import java.util.Comparator;
import java.util.Currency;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author al
 */
public class HistoricalCurrenciesComparatorNGTest {
    
    /**
     * Test of compare method, of class HistoricalCurrenciesComparator.
     */
    @Test
    public void testCompare() {
        System.out.println("compare");
        Currency currencyA = null;
        Currency currencyB = null;
        HistoricalCurrenciesComparator instance = new HistoricalCurrenciesComparator();
        int expResult = 0;
        int result = instance.compare(currencyA, currencyB);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
