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

import currency.CurrencyChooser;
import currency.CurrencyPair;

import java.util.Currency;
import java.util.Random;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the ExchangeRateProvider interface.
 * @author Alonso del Arte
 */
public class ExchangeRateProviderNGTest {
    
    static final Random RANDOM = new Random();
    
    private static class ExchangeRateProviderImpl 
            implements ExchangeRateProvider {
        
        private static int nonDefaultGetRateCallCount = 0;
        
        private static Currency mostRecentSource, mostRecentTarget;
        
        private static double mostRecentReturn = Double.NaN;
        
        @Override
        public double getRate(Currency source, Currency target) {
            nonDefaultGetRateCallCount++;
            return 1.0 + RANDOM.nextDouble();
        }
        
    }
    
}
