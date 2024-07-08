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
package currency.comparators;

import currency.CurrencyConverter;
import currency.MoneyAmount;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the ExchangeRateComparator class.
 * @author Alonso del Arte
 */
public class ExchangeRateComparatorNGTest {
    
    private static final List<String> CURRENCY_CODES = List.of("EUR", "USD");
    
    /**
     * Test of compare method, of class ExchangeRateComparator.
     */
    @Test
    public void testCompare() {
        System.out.println("compare");
        Currency currencyA = null;
        Currency currencyB = null;
        ExchangeRateComparator instance = null;
        int expResult = 0;
        int result = instance.compare(currencyA, currencyB);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
