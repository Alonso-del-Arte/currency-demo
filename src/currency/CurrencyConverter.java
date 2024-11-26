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

import currency.*;

import java.util.Currency;

/**
 * Converts currencies. To construct an instance of this class, an instance of 
 * {@link ExchangeRateProvider}, such as {@link 
 * MannysCurrencyConverterAPIAccess} is necessary.
 * @author Alonso del Arte
 */
public class CurrencyConverter {
    
    private final ExchangeRateProvider exchangeRateProvider;
    
    /**
     * Discloses the rate provider this converter is using.
     * @return The rate provider given to the constructor.
     */
    public ExchangeRateProvider getProvider() {
        return this.exchangeRateProvider;
    }
    
    /**
     * Converts a source amount of money to a target currency. The exchange rate 
     * provider that was given to the constructor is queried.
     * @param source The source amount of money. For example, $100.00 in United 
     * States dollars (USD).
     * @param target The target currency. For example, euros (EUR).
     * @return The converted amount, according to the given exchange rate 
     * provider. In the example, this might be 90,82&euro;.
     */
    public MoneyAmount convert(MoneyAmount source, Currency target) {
        double intermediate = source.getFullAmountInCents();
        Currency sourceCurrency = source.getCurrency();
        int sourceExponent = sourceCurrency.getDefaultFractionDigits();
        while (sourceExponent > 0) {
            intermediate /= 10.0;
            sourceExponent--;
        }
        double rate = this.exchangeRateProvider.getRate(sourceCurrency, target);
        double converted = intermediate * rate;
        double floored = Math.floor(converted);
        double roughDivs = converted - floored;
        int targetExponent = target.getDefaultFractionDigits();
        while (targetExponent > 0) {
            roughDivs *= 10.0;
            targetExponent--;
        }
        long units = (long) floored;
        short divisions = (short) Math.floor(roughDivs);
        return new MoneyAmount(units, target, divisions);
    }
    
    /**
     * Constructor.
     * @param rateProvider The rate provider to use. For example, an instance of 
     * {@link MannysCurrencyConverterAPIAccess}.
     */
    public CurrencyConverter(ExchangeRateProvider rateProvider) {
        if (rateProvider == null ) {
            String excMsg = "Rate provider should not be null";
            throw new NullPointerException(excMsg);
        }
        this.exchangeRateProvider = rateProvider;
    }
    
}
