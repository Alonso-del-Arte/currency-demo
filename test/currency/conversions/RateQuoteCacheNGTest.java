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
 * Tests of the RateQuoteCache class.
 * @author Alonso del Arte
 */
public class RateQuoteCacheNGTest {
    
    private static final int DEFAULT_CAPACITY = 8;
    
    /**
     * Test of the hasPair function, of the RateQuoteCache class.
     */
    @Test
    public void testHasPair() {
        System.out.println("hasPair");
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        RateQuoteCache instance = new RateQuoteCacheImpl(DEFAULT_CAPACITY);
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
        RateQuoteCache instance = new RateQuoteCacheImpl(DEFAULT_CAPACITY);
        String msg = "Since " + currencies.toString() 
                + " was not added to the cache, cache shouldn't have that pair";
        assert !instance.hasPair(currencies) : msg;
    }
    
    static List<CurrencyPair> listOtherPairs(CurrencyPair pair, int capacity) {
        int threshold = capacity - 1;
        Set<CurrencyPair> others = new HashSet<>(threshold);
        while (others.size() < threshold) {
            others.add(CurrencyChooser.choosePairOtherThan(pair));
        }
        List<CurrencyPair> list = new ArrayList<>(capacity);
        list.add(pair);
        list.addAll(others);
        return list;
    }
    
    @Test
    public void testCacheEvictsKeyAfterCapacityExhausted() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        int capacity = RANDOM.nextInt(LRUCache.MINIMUM_CAPACITY, 
                LRUCache.MAXIMUM_CAPACITY);
        RateQuoteCache instance = new RateQuoteCacheImpl(capacity);
        instance.retrieve(currencies);
        int initialCapacity = capacity 
                + RANDOM.nextInt(LRUCache.MINIMUM_CAPACITY) + 1;
        List<CurrencyPair> list = listOtherPairs(currencies, initialCapacity);
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
        int capacity = RANDOM.nextInt(LRUCache.MINIMUM_CAPACITY, 
                LRUCache.MAXIMUM_CAPACITY);
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
        RateQuoteCacheImpl instance = new RateQuoteCacheImpl(expected);
        List<CurrencyPair> list = listOtherPairs(currencies, expected);
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
    public void testRetrieveGivesQuoteFromCreateCall() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        int capacity = RANDOM.nextInt(LRUCache.MINIMUM_CAPACITY, 
                LRUCache.MAXIMUM_CAPACITY);
        RateQuoteCacheImpl instance = new RateQuoteCacheImpl(capacity);
        ConversionRateQuote actual = instance.retrieve(currencies);
        ConversionRateQuote expected = instance.mostRecentlyCreatedQuote;
        assertEquals(actual, expected);
    }

    /**
     * Test of the retrieve function, of the RateQuoteCache class.
     */
    @Test
    public void testRetrieve() {
        System.out.println("retrieve");
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        int capacity = RANDOM.nextInt(LRUCache.MINIMUM_CAPACITY, 
                LRUCache.MAXIMUM_CAPACITY);
        RateQuoteCacheImpl instance = new RateQuoteCacheImpl(capacity);
        ConversionRateQuote expected = instance.retrieve(currencies);
        int initialCapacity = capacity + 1;
        List<CurrencyPair> list = listOtherPairs(currencies, initialCapacity);
        for (int index = 1; index < capacity; index++) {
            CurrencyPair currCurrPair = list.get(index);
            instance.retrieve(currCurrPair);
            instance.retrieve(currencies);
        }
        instance.minutes = 0;
        ConversionRateQuote actual = instance.retrieve(currencies);
        assertEquals(actual, expected);
        for (int j = 1; j < initialCapacity; j++) {
            CurrencyPair currCurrPair = list.get(j);
            instance.retrieve(currCurrPair);
        }
        ConversionRateQuote potentialFreshQuote = instance.retrieve(currencies);
        String msg = "After evicting " + actual.toString() 
                + " from cache, it should not match fresh quote " 
                + potentialFreshQuote.toString();
        assert !actual.equals(potentialFreshQuote) : msg;
    }
    
    @Test
    public void testRetrieveRefreshesQuoteIfNeeded() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        int capacity = RANDOM.nextInt(LRUCache.MINIMUM_CAPACITY, 
                LRUCache.MAXIMUM_CAPACITY);
        RateQuoteCacheImpl instance = new RateQuoteCacheImpl(capacity);
        ConversionRateQuote unexpected = instance.retrieve(currencies);
        instance.minutes = 0;
        instance.refreshNeeded = true;
        ConversionRateQuote actual = instance.retrieve(currencies);
        String message = "Quote " + unexpected.toString() 
                + " was marked stale, should've been refreshed";
        assertNotEquals(actual, unexpected, message);
    }
    
    @Test
    public void testRetrieveDoesNotInvert() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        int capacity = RANDOM.nextInt(LRUCache.MINIMUM_CAPACITY, 
                LRUCache.MAXIMUM_CAPACITY);
        RateQuoteCacheImpl instance = new RateQuoteCacheImpl(capacity);
        ConversionRateQuote quoteA = instance.retrieve(currencies);
        int expected = instance.createCallCount + 1;
        ConversionRateQuote quoteB = instance.retrieve(currencies.flip());
        int actual = instance.createCallCount;
        String message = "Quote " + quoteA.toString() 
                + " should not have been inverted to obtain " 
                + quoteB.toString();
        assertEquals(actual, expected, message);
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
        for (int i = 1; i < LRUCache.MINIMUM_CAPACITY; i++) {
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
        int capacity = LRUCache.MAXIMUM_CAPACITY + RANDOM.nextInt(128) 
                + 1;
        String msg = "Capacity " + capacity + " in excess of maximum capacity "  
                + LRUCache.MAXIMUM_CAPACITY + " should cause an exception";
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
        
        private static final int MINUTES_IN_AN_HOUR = 60;
        
        int createCallCount = 0;
        
        ConversionRateQuote mostRecentlyCreatedQuote = null;
        
        int minutes = MINUTES_IN_AN_HOUR + RANDOM.nextInt(MINUTES_IN_AN_HOUR);
        
        boolean refreshNeeded = false;

        @Override
        protected ConversionRateQuote create(CurrencyPair currencies) {
            this.createCallCount++;
            LocalDateTime date = LocalDateTime.now().minusMinutes(minutes);
            this.mostRecentlyCreatedQuote = new ConversionRateQuote(currencies, 
                    RANDOM.nextDouble(), date);
            return this.mostRecentlyCreatedQuote;
        }
        
        @Override
        public boolean needsRefresh(CurrencyPair currencies) {
            return this.refreshNeeded;
        }
        
        public RateQuoteCacheImpl(int capacity) {
            super(capacity);
        }

    }
    
}
