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

import java.util.Currency;
import java.util.Set;

/**
 * Indicates that a class can support specific instances of {@code 
 * java.util.Currency} but not others. To query whether or not a specific 
 * currency is supported, call {@link #supports(java.util.Currency) supports()}. 
 * For the full set of supported currencies, call {@link 
 * #supportedCurrencies()}.
 * @author Alonso del Arte
 */
public interface SpecificCurrenciesSupport {
    
    /**
     * The currencies that are supported.
     * @return A set of currencies. For example, a set containing United States 
     * dollars (USD), euros (EUR) and the rest of the top 20 most traded 
     * currencies in the world.
     */
    Set<Currency> supportedCurrencies();
    
    /**
     * Tells whether or not a specific currency is supported. A default 
     * implementation is provided that calls {@link #supportedCurrencies()}. 
     * This function should be overridden for classes having a more efficient 
     * way of determining whether or not a currency is supported. For the 
     * examples, suppose this instance supports the 20 most traded currencies 
     * and no others.
     * @param currency The currency to query for support. Examples: the United 
     * States dollar (USD) and the Lebanese pound (LBP).
     * @return True if this instance supports the currency, false otherwise. In 
     * the example of an instance supporting the 20 most traded currencies, this 
     * function should return true for USD and false for LBP, which, unlike the 
     * North Korean won (KPW), can be traded in regulated markets, but very few 
     * people do.
     */
    default boolean supports(Currency currency) {
        Set<Currency> currencies = this.supportedCurrencies();
        return currencies.contains(currency);
    }
    
}
