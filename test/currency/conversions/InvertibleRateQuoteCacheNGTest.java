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
    
    private static class InvertibleRateQuoteCacheImpl 
            extends InvertibleRateQuoteCache {

        @Override
        public boolean needsRefresh(CurrencyPair currencies) {
            return false;
        }

        @Override
        protected ConversionRateQuote create(CurrencyPair name) {
            double rate = Math.random() + 0.5;
            return new ConversionRateQuote(name, rate);
        }

        public InvertibleRateQuoteCacheImpl(int capacity) {
            super(capacity);
        }
        
    }
    
}
