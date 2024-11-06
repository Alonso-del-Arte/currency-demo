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
    
    /**
     * Determines whether a pair of currencies is in this cache.
     * @param currencies The pair of currencies to look for. For example, United 
     * States dollars (USD) to euros (EUR).
     * @return True if this cache hasPair the specified pair, false otherwise.
     */
    boolean hasPair(CurrencyPair currencies) {
        boolean found = false;
        int i = 0;
        while (!found && i < this.names.length) {
            found = currencies.equals(this.names[i]);
            i++;
        }
        return found;
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
    }
    
}
