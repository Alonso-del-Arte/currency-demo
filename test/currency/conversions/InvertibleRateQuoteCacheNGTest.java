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
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the InvertibleRateQuoteCache class.
 * @author Alonso del Arte
 */
public class InvertibleRateQuoteCacheNGTest {
    
    @Test
    public void testSomeMethod() {
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
