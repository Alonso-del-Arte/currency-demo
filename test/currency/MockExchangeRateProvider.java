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

import java.util.Currency;

/**
 * Provides mock exchange rates. The purpose here is to test that a {@link 
 * CurrencyConverter} instance uses the exchange rate provider it's initialized 
 * with, and not whether or not the instance gives accurate or even plausible 
 * exchange rates.
 * @author Alonso del Arte
 */
public class MockExchangeRateProvider implements ExchangeRateProvider {
    
    private final ConversionRateQuote[] quotes;
    
    // TODO: Write tests for this
    @Override
    public double getRate(Currency source, Currency target) {
        CurrencyPair pair = new CurrencyPair(source, target);
        for (ConversionRateQuote quote : this.quotes) {
            if (quote.getCurrencies().equals(pair)) {
                return quote.getRate();
            }
        }
        return Double.NEGATIVE_INFINITY;
    }
        
    public MockExchangeRateProvider(ConversionRateQuote... rateQuotes) {
        this.quotes = rateQuotes;
    }
        
}
