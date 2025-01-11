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
    
    @Test
    public void testGetRate() {
        System.out.println("getRate");
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        ExchangeRateProviderImpl instance = new ExchangeRateProviderImpl();
        double actual = instance.getRate(currencies);
        double expected = instance.mostRecentReturn;
        double delta = 0.0001;
        String message = "Inquiring exchange rate from " + from.getDisplayName() 
                + " (" + from.getCurrencyCode() + ") to " + to.getDisplayName() 
                + " (" + to.getCurrencyCode() + ")";
        assertEquals(actual, expected, delta, message);
        assertEquals(instance.mostRecentSource, from);
        assertEquals(instance.mostRecentTarget, to);
    }
    
    private static class ExchangeRateProviderImpl 
            implements ExchangeRateProvider {
        
        private int nonDefaultGetRateCallCount = 0;
        
        private Currency mostRecentSource;
        
        private Currency mostRecentTarget;
        
        private double mostRecentReturn = Double.NaN;
        
        @Override
        public double getRate(Currency source, Currency target) {
            this.nonDefaultGetRateCallCount++;
            this.mostRecentSource = source;
            this.mostRecentTarget = target;
            this.mostRecentReturn = 1.0 + RANDOM.nextDouble();
            return this.mostRecentReturn;
        }
        
    }
    
}
