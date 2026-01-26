/*
 * Copyright (C) 2026 Alonso del Arte
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

import java.time.Year;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

import timeops.YearSpan;

/**
 * Tests of the CurrencyYearSpanDeterminer class.
 * @author Alonso del Arte
 */
public class CurrencyYearSpanDeterminerNGTest {
    
    /**
     * Test of determineYearSpan method, of class CurrencyYearSpanDeterminer.
     */
    @Test
    public void testDetermineYearSpan() {
        System.out.println("determineYearSpan");
        Currency currency = null;
        YearSpan expResult = null;
        YearSpan result = CurrencyYearSpanDeterminer.determineYearSpan(currency);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
