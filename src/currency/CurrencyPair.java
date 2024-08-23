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
 * Represents a pair of currencies. Preferably two distinct currencies. This is 
 * an immutable class. Its main purpose is to facilitate caching of recent 
 * results from a foreign exchange API.
 * @author Alonso del Arte
 */
public class CurrencyPair {
    
    private final Currency source, target;
    
    /**
     * Retrieves the From currency given to the constructor.
     * @return The From currency. For example, United States dollars (USD).
     */
    public Currency getFromCurrency() {
        return this.source;
    }
    
    /**
     * Retrieves the To currency given to the constructor.
     * @return The To currency. For example, euros (EUR).
     */
    public Currency getToCurrency() {
        return this.target;
    }
    
    // TODO: Write tests for this
    public CurrencyPair flip() {
        return this;
    }
    
    /**
     * Gives a textual representation of this currency pair. This is designed to 
     * be easy to put in a query to Manny's currency conversion API. For the 
     * example, let's say this pair is United States dollars (USD) and euros 
     * (EUR).
     * @return The ISO-4217 letter codes for the From and To currencies, 
     * separated by an underscore. For example, "USD_EUR".
     */
    @Override
    public String toString() {
        return this.source.getCurrencyCode() + '_' 
                + this.target.getCurrencyCode();
    }
    
    /**
     * Constructor.
     * @param from The From currency. For example, United States dollars (USD).
     * @param to The To currency. For example, euros (EUR). Ought to be 
     * different from {@code from}, but this is not checked.
     * @throws NullPointerException If either {@code from} or {@code to} is 
     * null.
     */
    public CurrencyPair(Currency from, Currency to) {
        if (from == null || to == null) {
            String excMsg = "From and To currencies should not be null";
            throw new NullPointerException(excMsg);
        }
        this.source = from;
        this.target = to;
    }
    
}
