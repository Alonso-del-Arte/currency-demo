/*
 * Copyright (C) 2026 Alonso del Arte
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
import java.util.Map;

/**
 *
 * @author Alonso del Arte
 */
public class WeightedExchangeRateProvider implements ExchangeRateProvider {

    // TODO: Write tests for this
    public Map<Currency, Double> getWeights() {
        return new java.util.HashMap<>();
    }
    
    // TODO: Write tests for this
    @Override
    public double getRate(Currency source, Currency target) {
        return -1.0;
    }

    // TODO: Write tests for this
    @Override
    public double getRate(CurrencyPair currencies) {
        return -1.0;
    }
    
    public WeightedExchangeRateProvider(Map<Currency, Double> weights, 
            ExchangeRateProvider rateProvider) {
        if (weights == null || rateProvider == null) {
            String excMsg = "Weights map, rate provider should not be null";
            throw new NullPointerException(excMsg);
        }
        // TODO: Write tests for this
    }
    
}
