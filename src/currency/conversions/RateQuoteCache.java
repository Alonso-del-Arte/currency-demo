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
import currency.CurrencyPair;

/**
 * Least recently used (LRU) cache for conversion rate quotes. The criterion for 
 * refreshing a quote is to be determined by the caller. Potentially fresh API 
 * calls will be made for currency pairs not already in the cache, even if the 
 * inverted pair is in the cache. If that inverting behavior is needed, use 
 * {@link InvertibleRateQuoteCache}.
 * @author Alonso del Arte
 */
public abstract class RateQuoteCache extends LRUCache<CurrencyPair, 
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
    public abstract boolean needsRefresh(CurrencyPair currencies);
    
    /**
     * Either retrieves a quote from the cache or creates it anew if it's not 
     * already in the cache. Each call for a quote that is not the very most 
     * recently retrieved will cause the contents of the cache to shift in some 
     * way. If the flipped quote is available, it will not be inverted; there 
     * will potentially be a fresh API call made. See {@link 
     * InvertibleRateQuoteCache}.
     * @param currencies The pair of currencies for which to retrieve a quote. 
     * For example, United States dollars (USD) to euros (EUR).
     * @return A conversion rate quote. For example, $1 = 0,91335&euro; as of 
     * October 11, 2024.
     */
    @Override
    public ConversionRateQuote retrieve(CurrencyPair currencies) {
        if (this.needsRefresh(currencies)) {
            this.refresh(currencies);
        }
        return super.retrieve(currencies);
    }
    
    /**
     * Sole constructor.
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
