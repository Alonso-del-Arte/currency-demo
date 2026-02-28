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
package currency.comparators;

import currency.conversions.ExchangeRateProvider;

import java.util.Comparator;
import java.util.Currency;

/** 
 * Compares currencies according to their exchange rates. The exchange rates are 
 * reckoned in relation to a base currency as given by a specified rate 
 * provider.
 * @author Alonso del Arte
 */
public class ExchangeRateComparator implements Comparator<Currency> {
    
    private final Currency baseCur;
    
    private final ExchangeRateProvider rateSupplier;
    
    public Currency getBaseCurrency() {
        return this.baseCur;
    }
    
    // TODO: Write tests for this
    public ExchangeRateProvider getRateProvider() {
        return new ExchangeRateProvider() {
            
            @Override
            public double getRate(Currency source, Currency target) {
                return 0;
            }
            
        };
    }
    
    /**
     * Compares two currencies according to how they exchange to the base 
     * currency. For the examples, suppose the euro (EUR) is the base currency. 
     * The quoted rates are from February 27, 2026.
     * @param currencyA The first currency to compare. For example, the Bahraini 
     * dinar (BHD). As of the date given above, one euro exchanges to 0.444 
     * dinars. 
     * @param currencyB The second currency to compare. For example, the 
     * Japanese yen. As of the date given above, one euro exchanges to 184&yen;.
     * @return &minus;1 or any negative integer if the base currency exchanges 
     * to less of {@code currencyA} than {@code currencyB}; 0 if they exchange 
     * the same; 1 or any positive integer if the base currency exchanges to 
     * more of {@code currencyA} than {@code currencyB}. With the examples, this 
     * function would most likely return &minus;1, or possibly some other 
     * negative integer.
     * @throws RuntimeException If some kind of {@code IOException} or other 
     * checked exception occurs, it will be wrapped into an unchecked exception, 
     * such as if the Internet connection drops out while making a query to an 
     * online API.
     */
    @Override
    public int compare(Currency currencyA, Currency currencyB) {
        double rateA = this.rateSupplier.getRate(this.baseCur, currencyA);
        double rateB = this.rateSupplier.getRate(this.baseCur, currencyB);
        return Double.compare(rateA, rateB);
    }
    
    /**
     * Constructor.
     * @param base The currency on which to base the comparisons. For example, 
     * the United States dollar (USD).
     * @param rateProvider A rate provider, such as one that connects to the 
     * Internet to obtain the latest exchange rates.
     */
    public ExchangeRateComparator(Currency base, 
            ExchangeRateProvider rateProvider) {
        this.baseCur = base;
        this.rateSupplier = rateProvider;
    }
  
}
