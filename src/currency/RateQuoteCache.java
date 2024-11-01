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

import cacheops.LRUCache;

/**
 * Least recently used (LRU) cache for conversion rate quotes. The criterion for 
 * refreshing a quote is to be determined by the caller.
 * @author Alonso del Arte
 */
abstract class RateQuoteCache extends LRUCache<CurrencyPair, 
        ConversionRateQuote> {
    
    private final ConversionRateQuote[] quotes;
    
    private int nextAvailableIndex = 0;
    
    private boolean cacheFull = false;
    
    /**
     * Adds a conversion rate quote to the cache that was not already there. It 
     * can certainly be the case that a quote for a particular currency pair was 
     * previously in the cache but was removed for reasons of capacity or 
     * freshness.
     * @param currencies The currency pair. For example, United States dollars 
     * (USD) and euros (EUR).
     * @return The new conversion rate quote. For example, $1 to &euro;0.89585 
     * as of September 19, 2024. 
     */
    @Override
    protected abstract ConversionRateQuote create(CurrencyPair currencies);
    
    private int indexOf(CurrencyPair currencies) {
        int i = 0;
        boolean found = false;
        int stop = this.cacheFull ? this.quotes.length 
                : this.nextAvailableIndex;
        while (!found && i < stop) {
            found = currencies.equals(this.quotes[i].getCurrencies());
            i++;
        }
        return found ? i - 1 : -1;
    }
    
    /**
     * Determines whether a pair of currencies is in this cache.
     * @param currencies The pair of currencies to look for. For example, United 
     * States dollars (USD) to euros (EUR).
     * @return True if this cache has the specified pair, false otherwise.
     */
    boolean has(CurrencyPair currencies) {
        return this.indexOf(currencies) > -1;
    }
    
    /**
     * Determines if the conversion rate quote needs to be refreshed. For some 
     * applications, it might be necessary to refresh quotes from a few minutes 
     * ago, in others quotes from a month ago might be acceptable. It's even 
     * possible to distinguish between frequently traded currencies like dollars 
     * and euros and less commonly traded currencies.
     * @param currencies The currency pair. For example, United States dollars 
     * (USD) and euros (EUR).
     * @return True if the conversion rate quote needs to be refreshed, false if 
     * not.
     */
    abstract boolean needsRefresh(CurrencyPair currencies);
    
    private static void moveArrayObjectToFront(Object[] objects, int index) {
        Object mostRecent = objects[index];
        for (int i = index; i > 0; i--) {
            objects[i] = objects[i - 1];
        }
        objects[0] = mostRecent;
    }
    
    /**
     * Either retrieves a quote from the cache or creates it anew if it's not 
     * already in the cache. Each call for a quote that is not the very most 
     * recently retrieve will cause the contents of the cache to shift in some 
     * way.
     * @param currencies The pair of currencies for which to retrieve a quote. 
     * For example, United States dollars (USD) to euros (EUR).
     * @return A conversion rate quote. For example, $1 = 0,91335&euro; as of 
     * October 11, 2024.
     */
    @Override
    public ConversionRateQuote retrieve(CurrencyPair currencies) {
        ConversionRateQuote quote;
        int index = this.indexOf(currencies);
        if (index < 0) {
            index = this.nextAvailableIndex;
            quote = this.create(currencies);
            this.quotes[index] = quote;
            this.nextAvailableIndex++;
        } else {
            if (this.needsRefresh(currencies)) {
                this.quotes[index] = this.create(currencies);
            }
            quote = this.quotes[index];
        }
        moveArrayObjectToFront(this.quotes, index);
        if (this.nextAvailableIndex == this.quotes.length) {
            this.nextAvailableIndex--;
            this.cacheFull = true;
        }
        return quote;
    }

    /**
     * Constructor.
     * @param capacity The capacity for the cache. For example, 32. Should be at 
     * least {@link cacheops.LRUCache#MINIMUM_CAPACITY} but not more than {@link 
     * cacheops.LRUCache#MAXIMUM_CAPACITY}.
     * @throws IllegalArgumentException If {@code capacity} is less than {@link 
     * cacheops.LRUCache#MINIMUM_CAPACITY} or more than {@link 
     * cacheops.LRUCache#MAXIMUM_CAPACITY}.
     */
    public RateQuoteCache(int capacity) {
        super(capacity);
        if (capacity < LRUCache.MINIMUM_CAPACITY 
                || capacity > LRUCache.MAXIMUM_CAPACITY) {
            String excMsg = "Capacity " + capacity + " is not valid";
            throw new IllegalArgumentException(excMsg);
        }
        this.quotes = new ConversionRateQuote[capacity];
    }
    
}
