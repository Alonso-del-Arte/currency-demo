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
 * Gives exchange rates from another provider multiplied by specified weights. 
 * The idea is that some exchange rates can be weighted in order to get a more 
 * meaningful comparison of "strength." For example, if 1&euro; exchanges to, 
 * say, &yen;110, the yen could be weighted so that the rate is 1.1 rather than 
 * 110.
 * @author Alonso del Arte
 */
public class WeightedExchangeRateProvider implements ExchangeRateProvider {
    
    private final Map<Currency, Double> currWeights;
    
    private final ExchangeRateProvider provider;

    /**
     * Retrieves the rates as provided to the constructor at the time of 
     * construction.
     * @return The weights. For example, Japanese yen (JPY) weighted to 0.01 and 
     * a couple other currencies with appropriate weights.
     */
    public Map<Currency, Double> getWeights() {
        return new HashMap<>(this.currWeights);
    }
    
    /**
     * Gives the rate to convert one unit of the source currency to the target 
     * currency. If a weight is specified for the target currency, then that 
     * value is used. Otherwise the weight is 1.0.
     * @param source The source to convert from. For example, United States 
     * dollars (USD).
     * @param target The target to convert one unit of {@code source} to. For 
     * example, the Japanese yen (JPY).
     * @return The rate of conversion, multiplied by the weight. In the example, 
     * if the rate given by the provider is 158.6152 but JPY is weighted to 
     * 0.01, then this function would return 1.586152.
     */
    @Override
    public double getRate(Currency source, Currency target) {
        double weight = 1.0;
        if (this.currWeights.containsKey(target)) {
            weight = this.currWeights.get(target);
        }
        return this.provider.getRate(source, target) * weight;
    }

    /**
     * Gives the rate to convert one unit of the source currency to the target 
     * currency. If a weight is specified for the target currency, then that 
     * value is used. Otherwise the weight is 1.0.
     * @param currencies The pair of currencies, source and target. For example, 
     * United States dollars (USD) and Japanese yen (JPY). 
     * @return The rate of conversion, multiplied by the weight. In the example, 
     * if the rate given by the provider is 158.6152 but JPY is weighted to 
     * 0.01, then this function would return 1.586152.
     */
    @Override
    public double getRate(CurrencyPair currencies) {
        return this.getRate(currencies.getFromCurrency(), 
                currencies.getToCurrency());
    }
    
    /**
     * Constructor.
     * @param weights A map matching currencies to weights. For example, 
     * Japanese yen (JPY) weighted to 0.01, and a couple other currencies 
     * similarly weighted. It's not necessary to specify all currencies. Those 
     * currencies without a specified weight will default to a weight of 1.0.
     * @param rateProvider The rate provider to use. If nothing else, {@link 
     * HardCodedRateProvider} should always be available.
     * @throws NullPointerException If {@code weights} or {@code rateProvider} 
     * is null.
     */
    public WeightedExchangeRateProvider(Map<Currency, Double> weights, 
            ExchangeRateProvider rateProvider) {
        if (weights == null || rateProvider == null) {
            String excMsg = "Weights map, rate provider should not be null";
            throw new NullPointerException(excMsg);
        }
        this.currWeights = new HashMap<>(weights);
        this.provider = rateProvider;
    }
    
}
