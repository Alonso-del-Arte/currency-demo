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

/**
 *
 * @author Alonso del Arte
 */
abstract class RateQuoteCache {
    
    private ConversionRateQuote[] quotes = new ConversionRateQuote[10];
    
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
    abstract ConversionRateQuote create(CurrencyPair currencies);
    
    // TODO: Write tests for this
    boolean has(CurrencyPair currencies) {
        return false;
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
    
    // TODO: Write tests for this
    ConversionRateQuote retrieve(CurrencyPair currencies) {
        return new ConversionRateQuote(currencies, 0.0);
    }

    public RateQuoteCache(int capacity) {
    }
    
}
