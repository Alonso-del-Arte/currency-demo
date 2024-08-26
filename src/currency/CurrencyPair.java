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
     * Determines if this currency pair is equal to another object. For the 
     * examples, suppose this currency pair is from United States dollars (USD) 
     * to euros (EUR).
     * @param obj The object to compare against. Examples: a currency pair from 
     * USD to EUR, a currency pair from USD to Canadian dollars (CAD), a 
     * currency pair from CAD to EUR, the {@code Currency} instance for CAD, and 
     * a null.
     * @return True if {@code obj} matches this currency pair in both the From 
     * and To currencies, false in all other cases. In the examples, this would 
     * be true for the USD to EUR currency pair, false for the USD to CAD pair 
     * (To currencies don't match), false for the CAD to EUR pair (From 
     * currencies don't match), false for the CAD instance, and certainly false 
     * for the null.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }
        CurrencyPair other = (CurrencyPair) obj;
        if (!this.source.equals(other.source)) {
            return false;
        }
        return this.target.equals(other.target);
    }
    
    /**
     * Gives a hash code for this currency pair. The hash code is based on the 
     * 3-digit codes from ISO-4217. I reserve the right to change the 
     * mathematical formula in a later version of this class.
     * @return A hash code based on the From and To currencies' 3-digit codes in 
     * ISO-4217. For example, for a pair from United States dollars (USD) to 
     * euros (EUR), this might be 55051218 = 840 &times; 65536 + 978.
     */
    @Override
    public int hashCode() {
        return (this.source.getNumericCode() << 16) 
                + this.target.getNumericCode();
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
