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
package currency;

import static currency.CurrencyChooser.RANDOM;

import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

import org.testng.annotations.Test;

/**
 * Tests of the SpecificCurrenciesSupport class.
 * @author Alonso del Arte
 */
public class SpecificCurrenciesSupportTest {
    
    private static final Set<Currency> ALL_CURRENCIES 
            = Currency.getAvailableCurrencies();
    
    private static Set<Currency> makeSubset() {
        int leastSignificantDigit = RANDOM.nextInt(10);
        Set<Currency> subset = new HashSet<>();
        for (Currency currency : ALL_CURRENCIES) {
            if (currency.getNumericCode() % 10 == leastSignificantDigit) {
                subset.add(currency);
            }
        }
        return subset;
    }
    
    @Test
    public void testSupports() {
        System.out.println("supports");
        Set<Currency> currencies = makeSubset();
        SpecificCurrenciesSupport instance 
                = new SpecificCurrenciesSupportImpl(currencies);
        for (Currency currency : currencies) {
            String msg = "Currency " + currency.getDisplayName() + " (" 
                    + currency.getCurrencyCode() + ", " 
                    + currency.getNumericCodeAsString() 
                    + ") should be supported";
            assert instance.supports(currency) : msg;
        }
    }
    
    @Test
    public void testDoesNotSupport() {
        Set<Currency> currencies = makeSubset();
        SpecificCurrenciesSupport instance 
                = new SpecificCurrenciesSupportImpl(currencies);
        Set<Currency> complement = new HashSet<>(ALL_CURRENCIES);
        complement.removeAll(currencies);
        String msgPart = "\u0029 is not in the set " + currencies.toString() 
                + ", it should not be supported";
        for (Currency currency : complement) {
            String msg = "As currency " + currency.getDisplayName() + " \u0028" 
                    + currency.getCurrencyCode() + ", " 
                    + currency.getNumericCodeAsString() + msgPart;
            assert !instance.supports(currency) : msg;
        }
    }
    
    private static class SpecificCurrenciesSupportImpl 
            implements SpecificCurrenciesSupport {
        
        private final Set<Currency> supportedCurrencies;
        
        @Override
        public Set<Currency> supportedCurrencies() {
            return this.supportedCurrencies;
        }
        
        private SpecificCurrenciesSupportImpl(Set<Currency> currencies) {
            this.supportedCurrencies = currencies;
        }
        
    }
    
}
