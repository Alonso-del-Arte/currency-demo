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
package currency.conversions;

import currency.CurrencyPair;

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
    
    /**
     * Gives a conversion rate.
     * @param source The source currency. For example, United States dollar 
     * (USD).
     * @param target The target currency. For example, euro (EUR).
     * @return Either a rate specified by one of the quotes given to the 
     * constructor, or 1.0 if the particular combination was not specified to 
     * the constructor, even if the opposite combination was given to the 
     * constructor (this function will not try to deduce the rate).
     */
    @Override
    public double getRate(Currency source, Currency target) {
        CurrencyPair pair = new CurrencyPair(source, target);
        for (ConversionRateQuote quote : this.quotes) {
            if (quote.getCurrencies().equals(pair)) {
                return quote.getRate();
            }
        }
        return 1.0;
    }
        
    /**
     * Constructor.
     * @param rateQuotes A group of rate quotes. For example, United States 
     * dollar (USD) to Japanese yen (JPY) at a rate of 141.396985 and euro (EUR) 
     * to Brazilian real (BRL) at a rate of 6.257201, both quotes as of 
     * September 11, 2024.
     */
    public MockExchangeRateProvider(ConversionRateQuote... rateQuotes) {
        this.quotes = rateQuotes;
    }
        
}
