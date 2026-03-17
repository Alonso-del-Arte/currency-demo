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
import java.util.HashMap;
import java.util.Map;

/**
 * WORK IN PROGRESS...
 * The idea is that some exchange rates can be weighted in order to get a more 
 * meaningful comparison of "strength." For example, if 1&euro; exchanges to, 
 * say, &yen;110, the yen could be weighted so that the rate is 1.1 rather than 
 * 110.
 * @author Alonso del Arte
 */
public class WeightedExchangeRateProvider implements ExchangeRateProvider {
    
    private final Map<Currency, Double> currWeights;
    
    private final ExchangeRateProvider provider;

    // TODO: Write tests for this
    public Map<Currency, Double> getWeights() {
        return new HashMap<>(this.currWeights);
    }
    
    // TODO: Write tests for this
    @Override
    public double getRate(Currency source, Currency target) {
        if (target == null) {
            throw new NullPointerException("Target should not be null");
        }
        return this.provider.getRate(source, target);
    }

    // TODO: Write tests for this
    @Override
    public double getRate(CurrencyPair currencies) {
        if (currencies == null) {
            return -1.0;
        }
        return this.provider.getRate(currencies);
    }
    
    public WeightedExchangeRateProvider(Map<Currency, Double> weights, 
            ExchangeRateProvider rateProvider) {
        if (weights == null || rateProvider == null) {
            String excMsg = "Weights map, rate provider should not be null";
            throw new NullPointerException(excMsg);
        }
        this.currWeights = weights;
        this.provider = rateProvider;
    }
    
}
