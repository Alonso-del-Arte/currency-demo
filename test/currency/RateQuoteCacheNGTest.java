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

import java.time.LocalDateTime;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the RateQuoteCache class.
 * @author Alonso del Arte
 */
public class RateQuoteCacheNGTest {
    
    @Test
    public void testMinimumCapacityConstant() {
        assertEquals(RateQuoteCache.MINIMUM_CAPACITY, 4);
    }
    
    @Test
    public void testMaximumCapacityConstant() {
        assertEquals(RateQuoteCache.MAXIMUM_CAPACITY, 128);
    }
    
    /**
     * Test of create method, of class RateQuoteCache.
     */
//    @Test
    public void testCreate() {
        System.out.println("create");
        CurrencyPair currencies = null;
        RateQuoteCache instance = null;
        ConversionRateQuote expResult = null;
        ConversionRateQuote result = instance.create(currencies);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of has method, of class RateQuoteCache.
     */
//    @Test
    public void testHas() {
        System.out.println("has");
        CurrencyPair currencies = null;
        RateQuoteCache instance = null;
        boolean expResult = false;
        boolean result = instance.has(currencies);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of retrieve method, of class RateQuoteCache.
     */
//    @Test
    public void testRetrieve() {
        System.out.println("retrieve");
        CurrencyPair currencies = null;
        RateQuoteCache instance = null;
        ConversionRateQuote expResult = null;
        ConversionRateQuote result = instance.retrieve(currencies);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    private static class RateQuoteCacheImpl extends RateQuoteCache {

        @Override
        public ConversionRateQuote create(CurrencyPair currencies) {
            return null;
        }
        
        @Override
        boolean needsRefresh(CurrencyPair currencies) {
            return true;
        }
        
        public RateQuoteCacheImpl() {
            super(10);
        }

    }
    
}
