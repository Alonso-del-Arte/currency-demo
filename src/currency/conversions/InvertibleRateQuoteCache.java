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
 * WORK IN PROGRESS...
 * The idea here is that when possible this will infer quotes rather than make a 
 * new API call when it's possible to do so. For example, if the cache has the 
 * rate for United States dollars (USD) to euros (EUR) and receives a request 
 * for EUR to USD, the cache will invert the USD to EUR rate quote rather than 
 * make an API call for EUR to USD, provided that the quote to be inverted is 
 * not stale.
 * @author Alonso del Arte
 */
public abstract class InvertibleRateQuoteCache extends RateQuoteCache {
    
    @Override
    boolean hasPair(CurrencyPair currencies) {
        boolean found = false;
        int i = 0;
        while (!found && i < this.names.length) {
            found = currencies.equals(this.names[i]);
            i++;
        }
        return found;
    }
    
    // TODO: Write tests for this
    @Override
    public ConversionRateQuote retrieve(CurrencyPair currencies) {
        this.create(currencies);
        super.retrieve(currencies);
        return new ConversionRateQuote(currencies, -1.0);
    }
    
    public InvertibleRateQuoteCache(int capacity) {
        super(capacity);
    }
    
}
