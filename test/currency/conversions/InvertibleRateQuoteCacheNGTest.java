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
package currency.conversions;

import cacheops.LRUCache;
import currency.CurrencyChooser;
import currency.CurrencyPair;
import static currency.conversions.ExchangeRateProviderNGTest.RANDOM;

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
 * Tests of the InvertibleRateQuoteCache class.
 * @author Alonso del Arte
 */
public class InvertibleRateQuoteCacheNGTest {
    
    private static final int DEFAULT_CAPACITY = 8;
    
    @Test
    public void testHasPair() {
        System.out.println("hasPair");
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        RateQuoteCache instance 
                = new InvertibleRateQuoteCacheImpl(DEFAULT_CAPACITY);
        instance.retrieve(currencies);
        String msg = "Right after adding " + currencies.toString() 
                + " to the cache, cache should have that pair";
        assert instance.hasPair(currencies) : msg;
    }
    
    @Test
    public void testDoesNotHavePair() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        RateQuoteCache instance 
                = new InvertibleRateQuoteCacheImpl(DEFAULT_CAPACITY);
        String msg = "Since " + currencies.toString() 
                + " was not added to the cache, cache shouldn't have that pair";
        assert !instance.hasPair(currencies) : msg;
    }
    
    @Test
    public void testCacheEvictsKeyAfterCapacityExhausted() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        int capacity = RANDOM.nextInt(LRUCache.MINIMUM_CAPACITY + 1, 
                LRUCache.MAXIMUM_CAPACITY - 1);
        RateQuoteCache instance = new InvertibleRateQuoteCacheImpl(capacity);
        instance.retrieve(currencies);
        int initialCapacity = capacity 
                + RANDOM.nextInt(LRUCache.MINIMUM_CAPACITY) + 1;
        List<CurrencyPair> list = RateQuoteCacheNGTest
                .listOtherPairs(currencies, initialCapacity);
        for (int index = 1; index < initialCapacity; index++) {
            CurrencyPair currCurrPair = list.get(index);
            instance.retrieve(currCurrPair);
        }
        String msg = "Having retrieved rate for " + currencies.toString() 
                + " and then " + (initialCapacity - 1) 
                + " others, cache of capacity " + capacity 
                + " should not retain " + currencies.toString();
        assert !instance.hasPair(currencies) : msg;
    }
    
    @Test
    public void testCacheRetainsKeyWhileCapacityAvailable() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        int capacity = RANDOM.nextInt(LRUCache.MINIMUM_CAPACITY + 1, 
                LRUCache.MAXIMUM_CAPACITY - 1);
        RateQuoteCache instance = new InvertibleRateQuoteCacheImpl(capacity);
        instance.retrieve(currencies);
        List<CurrencyPair> list = RateQuoteCacheNGTest
                .listOtherPairs(currencies, capacity);
        for (int index = 1; index < capacity; index++) {
            CurrencyPair currCurrPair = list.get(index);
            instance.retrieve(currCurrPair);
            String msg = "Having retrieved rate for " + currencies.toString() 
                    + " and then " + index + " other(s) in a cache of capacity " 
                    + capacity + ", including " + currCurrPair.toString() 
                    + ", cache should still have " + currencies.toString();
            assert instance.hasPair(currencies) : msg;
        }
    }

    @Test
    public void testCacheRetainsFrequentlyUsedKey() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        int expected = RANDOM.nextInt(LRUCache.MINIMUM_CAPACITY, 
                LRUCache.MAXIMUM_CAPACITY);
        InvertibleRateQuoteCacheImpl instance 
                = new InvertibleRateQuoteCacheImpl(expected);
        List<CurrencyPair> list = RateQuoteCacheNGTest
                .listOtherPairs(currencies, expected);
        for (int index = 1; index < expected; index++) {
            instance.retrieve(currencies);
            CurrencyPair currCurrPair = list.get(index);
            instance.retrieve(currCurrPair);
        }
        int actual = instance.createCallCount;
        String message = "Having frequently retrieved rate for " 
                + currencies.toString() + ", create call count should be " 
                + expected + ", with just one call for that pair";
        assertEquals(actual, expected, message);
    }

    @Test
    public void testConstructorRejectsNegativeCapacity() {
        int capacity = -RANDOM.nextInt(128) - 1;
        String msg = "Capacity " + capacity + " should cause an exception";
        Throwable t = assertThrows(() -> {
            RateQuoteCache badCache 
                    = new InvertibleRateQuoteCacheImpl(capacity);
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
            RateQuoteCache badCache 
                    = new InvertibleRateQuoteCacheImpl(capacity);
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
        for (int i = 1; i < LRUCache.MINIMUM_CAPACITY; i++) {
            final int capacity = i;
            String msg = "Capacity " + capacity + " should cause an exception";
            Throwable t = assertThrows(() -> {
            RateQuoteCache badCache 
                    = new InvertibleRateQuoteCacheImpl(capacity);
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
        int capacity = LRUCache.MAXIMUM_CAPACITY + RANDOM.nextInt(128) 
                + 1;
        String msg = "Capacity " + capacity + " in excess of maximum capacity "  
                + LRUCache.MAXIMUM_CAPACITY + " should cause an exception";
        Throwable t = assertThrows(() -> {
            RateQuoteCache badCache 
                    = new InvertibleRateQuoteCacheImpl(capacity);
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

    private static class InvertibleRateQuoteCacheImpl 
            extends InvertibleRateQuoteCache {

        int createCallCount = 0;
        
        @Override
        public boolean needsRefresh(CurrencyPair currencies) {
            return false;
        }

        @Override
        protected ConversionRateQuote create(CurrencyPair name) {
            this.createCallCount++;
            double rate = Math.random() + 0.5;
            return new ConversionRateQuote(name, rate);
        }

        public InvertibleRateQuoteCacheImpl(int capacity) {
            super(capacity);
        }
        
    }
    
}
