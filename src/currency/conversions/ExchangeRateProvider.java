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
 * Defines one function for classes that provide access to a currency conversion 
 * rate API.
 * @author Alonso del Arte
 */
public interface ExchangeRateProvider {
    
    /**
     * Gives the rate to convert one unit of the source currency to the target 
     * currency.
     * @param source The source to convert from. For example, United States 
     * dollars (USD).
     * @param target The target to convert one unit of {@code source} to. For 
     * example, euros (EUR).
     * @return The conversion rate. In the example as of August 12, 2024, 
     * 0.915796.
     * @throws RuntimeException If some kind of {@code IOException} or other 
     * checked exception occurs, it will be wrapped into an unchecked exception.
     */
    double getRate(Currency source, Currency target);
    
    // TODO: Write tests for this
    default double getRate(CurrencyPair currencies) {
        return 0.0;
    }
    
}
