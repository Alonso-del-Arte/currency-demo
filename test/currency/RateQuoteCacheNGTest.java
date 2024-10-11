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

import static currency.CurrencyChooser.RANDOM;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testframe.api.Asserters.assertThrows;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the RateQuoteCache class.
 * @author Alonso del Arte
 */
public class RateQuoteCacheNGTest {
    
    private static final int DEFAULT_CAPACITY = 8;
    
    @Test
    public void testMinimumCapacityConstant() {
        assertEquals(RateQuoteCache.MINIMUM_CAPACITY, 4);
    }
    
    @Test
    public void testMaximumCapacityConstant() {
        assertEquals(RateQuoteCache.MAXIMUM_CAPACITY, 128);
    }
    
    /**
     * Test of the has function, of the RateQuoteCache class.
     */
    @Test
    public void testHas() {
        System.out.println("has");
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        RateQuoteCache instance = new RateQuoteCacheImpl(DEFAULT_CAPACITY);
        instance.retrieve(currencies);
        String msg = "Right after adding " + currencies.toString() 
                + " to the cache, cache should have that pair";
        assert instance.has(currencies) : msg;
    }

    @Test
    public void testDoesNotHave() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        RateQuoteCache instance = new RateQuoteCacheImpl(DEFAULT_CAPACITY);
        String msg = "Since " + currencies.toString() 
                + " was not added to the cache, cache shouldn't have that pair";
        assert !instance.has(currencies) : msg;
    }
    
    private static List<CurrencyPair> listOtherPairs(CurrencyPair pair, 
            int initialCapacity) {
        int threshold = initialCapacity - 1;
        Set<CurrencyPair> others = new HashSet<>(threshold);
        while (others.size() < threshold) {
            others.add(CurrencyChooser.choosePairOtherThan(pair));
        }
        List<CurrencyPair> list = new ArrayList<>(initialCapacity);
        list.add(pair);
        list.addAll(others);
        return list;
    }
    
    @Test
    public void testCacheEvictsKeyAfterCapacityExhausted() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        int capacity = RANDOM.nextInt(RateQuoteCache.MINIMUM_CAPACITY, 
                RateQuoteCache.MAXIMUM_CAPACITY);
        RateQuoteCache instance = new RateQuoteCacheImpl(capacity);
        instance.retrieve(currencies);
        int initialCapacity = capacity 
                + RANDOM.nextInt(RateQuoteCache.MINIMUM_CAPACITY) + 1;
        List<CurrencyPair> list = listOtherPairs(currencies, initialCapacity);
        for (int index = 1; index < initialCapacity; index++) {
            CurrencyPair currCurrPair = list.get(index);
            instance.retrieve(currCurrPair);
        }
        String msg = "Having retrieved rate for " + currencies.toString() 
                + " and then " + (initialCapacity - 1) 
                + " others, cache of capacity " + capacity 
                + " should not retain " + currencies.toString();
        assert !instance.has(currencies) : msg;
    }
    
    @Test
    public void testCacheRetainsKeyWhileCapacityAvailable() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        int capacity = RANDOM.nextInt(RateQuoteCache.MINIMUM_CAPACITY, 
                RateQuoteCache.MAXIMUM_CAPACITY);
        RateQuoteCache instance = new RateQuoteCacheImpl(capacity);
        instance.retrieve(currencies);
        List<CurrencyPair> list = listOtherPairs(currencies, capacity);
        for (int index = 1; index < capacity; index++) {
            CurrencyPair currCurrPair = list.get(index);
            instance.retrieve(currCurrPair);
            String msg = "Having retrieved rate for " + currencies.toString() 
                    + " and then " + index + " other(s) in a cache of capacity " 
                    + capacity + ", including " + currCurrPair.toString() 
                    + ", cache should still have " + currencies.toString();
            assert instance.has(currencies) : msg;
        }
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
    
    @Test
    public void testConstructorRejectsNegativeCapacity() {
        int capacity = -RANDOM.nextInt(128) - 1;
        String msg = "Capacity " + capacity + " should cause an exception";
        Throwable t = assertThrows(() -> {
            RateQuoteCache badCache = new RateQuoteCacheImpl(capacity);
            System.out.println(msg + ", not given instance " 
                    + badCache.toString());
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        String numStr = Integer.toString(capacity);
        String capMsg = "Exception message should contain \"" + numStr + "\"";
        assert excMsg.contains(numStr) : capMsg;
        System.out.println("\"" + excMsg + "\"");
    }

    @Test
    public void testConstructorRejectsCapacityZero() {
        int capacity = 0;
        String msg = "Capacity " + capacity + " should cause an exception";
        Throwable t = assertThrows(() -> {
            RateQuoteCache badCache = new RateQuoteCacheImpl(capacity);
            System.out.println(msg + ", not given instance " 
                    + badCache.toString());
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        String numStr = Integer.toString(capacity);
        String capMsg = "Exception message should contain \"" + numStr + "\"";
        assert excMsg.contains(numStr) : capMsg;
        System.out.println("\"" + excMsg + "\"");
    }
    
    @Test
    public void testConstructorRejectsLowPositiveCapacity() {
        for (int i = 1; i < RateQuoteCache.MINIMUM_CAPACITY; i++) {
            final int capacity = i;
            String msg = "Capacity " + capacity + " should cause an exception";
            Throwable t = assertThrows(() -> {
                RateQuoteCache badCache = new RateQuoteCacheImpl(capacity);
                System.out.println(msg + ", not given instance " 
                        + badCache.toString());
            }, IllegalArgumentException.class, msg);
            String excMsg = t.getMessage();
            assert excMsg != null : "Exception message should not be null";
            assert !excMsg.isBlank() : "Exception message should not be blank";
            String numStr = Integer.toString(i);
            String capMsg = "Exception message should contain \"" + numStr 
                    + "\"";
            assert excMsg.contains(numStr) : capMsg;
            System.out.println("\"" + excMsg + "\"");
        }
    }

    @Test
    public void testConstructorRejectsExcessiveCapacity() {
        int capacity = RateQuoteCache.MAXIMUM_CAPACITY + RANDOM.nextInt(128) 
                + 1;
        String msg = "Capacity " + capacity + " in excess of maximum capacity "  
                + RateQuoteCache.MAXIMUM_CAPACITY 
                + " should cause an exception";
        Throwable t = assertThrows(() -> {
            RateQuoteCache badCache = new RateQuoteCacheImpl(capacity);
            System.out.println(msg + ", not given instance " 
                    + badCache.toString());
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        String numStr = Integer.toString(capacity);
        String capMsg = "Exception message should contain \"" + numStr + "\"";
        assert excMsg.contains(numStr) : capMsg;
        System.out.println("\"" + excMsg + "\"");
    }

    private static class RateQuoteCacheImpl extends RateQuoteCache {
        
        int createCallCount = 0;

        @Override
        public ConversionRateQuote create(CurrencyPair currencies) {
            this.createCallCount++;
            return new ConversionRateQuote(currencies, RANDOM.nextDouble());
        }
        
        @Override
        boolean needsRefresh(CurrencyPair currencies) {
            return false;
        }
        
        public RateQuoteCacheImpl(int capacity) {
            super(capacity);
        }

    }
    
}
