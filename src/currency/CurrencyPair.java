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
    
    /**
     * Constructor.
     * @param from The From currency. For example, United States dollars (USD).
     * @param to The To currency. For example, euros (EUR).
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
