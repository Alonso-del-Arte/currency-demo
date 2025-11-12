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

import currency.CurrencyPair;

/**
 * Least recently used (LRU) cache for conversion rate quotes that inverts a 
 * rate quote rather than make a new API call when it's possible to do that. For 
 * example, if the cache has the rate for United States dollars (USD) to euros 
 * (EUR) and receives a request for EUR to USD, the cache will invert the USD to 
 * EUR rate quote rather than make an API call for EUR to USD, provided that the 
 * quote to be inverted is not stale as determined by {@link 
 * #needsRefresh(currency.CurrencyPair) needsRefresh()}.
 * @author Alonso del Arte
 */
public abstract class InvertibleRateQuoteCache extends RateQuoteCache {
    
    /**
     * Either retrieves a quote from the cache or creates it anew if it's not 
     * already in the cache. Each call for a quote that is not the very most 
     * recently retrieved will cause the contents of the cache to shift in some 
     * way. If the flipped quote is available, it will be inverted if it's not 
     * stale as determined by {@link #needsRefresh(currency.CurrencyPair) 
     * needsRefresh()}. If that behavior is not wanted, use a different subclass 
     * of {@link RateQuoteCache}.
     * @param currencies The pair of currencies for which to retrieve a quote. 
     * For example, United States dollars (USD) to euros (EUR).
     * @return A conversion rate quote. For example, $1 = 0,86237&euro; as of 
     * November 11, 2025.
     */
    @Override
    public ConversionRateQuote retrieve(CurrencyPair currencies) {
        CurrencyPair flipped = currencies.flip();
        if (!this.hasPair(currencies) && this.hasPair(flipped)) {
            return super.retrieve(flipped).invert();
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
    public InvertibleRateQuoteCache(int capacity) {
        super(capacity);
    }
    
}
