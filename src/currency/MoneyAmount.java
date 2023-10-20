/*
 * Copyright (C) 2023 Alonso del Arte
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
 * Represents an amount of money of a specific currency.
 * @author Alonso del Arte
 */
public class MoneyAmount implements Comparable<MoneyAmount> {
    
    private final long singles;
    
    private final short cents;
    
    private final int multiplier;
    
    private final long allCents;
    
    private final Currency currencyID;
    
    private static int calculateMultiplier(Currency currency) {
        int mult = 1;
        int multCount = 0;
        int stop = currency.getDefaultFractionDigits();
        while (multCount < stop) {
            mult *= 10;
            multCount++;
        }
        return mult;
    }
    
    /**
     * Tells how many full units of currency there are in the amount. Any cents, 
     * mills, darahim, etc., are ignored.
     * @return The number of units. For example, if the amount is &euro;197.54, 
     * this function returns 197.
     */
    public long getUnits() {
        return this.singles;
    }
    
    /**
     * Tells how many divisions of the unit of currency there are in the amount. 
     * The units are multiplied as needed and the divisions are added.
     * @return The number of divisions of the unit of currency. For example, if 
     * the amount is &euro;197.54, this function returns 19754.
     */
    public long getFullAmountInCents() {
        return this.allCents;
    }
    
    /**
     * Tells how many divisions of the unit of currency there are in the amount 
     * minus the full units.
     * @return The number of divisions of the unit of currency minus the full 
     * units. For example, if the amount is &euro;197.54, this function returns 
     * 54.
     */
    public long getDivisions() {
        return this.cents;
    }
    
    /**
     * Tells what currency this money amount is drawn in.
     * @return The currency. For example, if the amount is &euro;197.54, this 
     * function returns EUR.
     */
    public Currency getCurrency() {
        return this.currencyID;
    }
    
    // TODO: Write tests for this
    public MoneyAmount plus(MoneyAmount addend) {
        return this;
    }
    
    // TODO: Write tests for this
    public MoneyAmount negate() {
        return this;
    }
    
    // TODO: Write tests for this
    public MoneyAmount minus(MoneyAmount subtrahend) {
        return this;
    }
    
    // TODO: Write tests for this
    public MoneyAmount times(int multiplicand) {
        return this;
    }
    
    // TODO: Write tests for this
    public MoneyAmount divides(int divisor) {
        return this;
    }
    
    // TODO: Write tests for this
    @Override
    public int compareTo(MoneyAmount other) {
        return 0;
    }

    @Override
    public String toString() {
        if (this.multiplier == 1) {
            return this.currencyID.getSymbol() + this.singles;
        }
        String intermediate = this.currencyID.getSymbol() 
                + Math.abs(this.singles) + '.';
        if (this.singles < 0) {
            intermediate = '-' + intermediate;
        }
        if (this.cents < 10) {
            intermediate += "0";
        }
        if (this.cents < 100 && this.multiplier == 1000) {
            intermediate += "0";
        }
        return intermediate + this.cents;
    }
    
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
        final MoneyAmount other = (MoneyAmount) obj;
        if (this.currencyID != other.currencyID) {
            return false;
        }
        return this.allCents == other.allCents;
    }
    
    @Override
    public int hashCode() {
        return (this.currencyID.hashCode() << 16) + (int) this.allCents;
    }
    
    // TODO: Write tests for this
    public MoneyAmount(long units, Currency currency) {
        this(units, currency, (short) 0);
    }

    // TODO: Write tests for this
    public MoneyAmount(long units, Currency currency, short divisions) {
        this.singles = units;
        this.cents = divisions;
        this.multiplier = calculateMultiplier(currency);
        this.currencyID = currency;
        this.allCents = this.singles * this.multiplier + this.cents;
    }

    // TODO: Write tests for this
    private MoneyAmount(Currency currency, long fullAmountInCents, 
            int verifiedMultiplier) {
        this.singles = -1;
        this.cents = -1;
        this.multiplier = calculateMultiplier(currency);
        this.currencyID = currency;
        this.allCents = this.singles * this.multiplier + this.cents;
    }    
}
