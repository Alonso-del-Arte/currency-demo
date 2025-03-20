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

/**
 * Represents an amount of money of a specific currency. The precision is 
 * limited to the number of default fraction digits. Thus, for example, for a 
 * currency in which the units are normally divided into 100 cents, mills in 
 * intermediate calculations are discarded.
 * @author Alonso del Arte
 */
public class MoneyAmount implements Comparable<MoneyAmount> {
    
    private final long singles;
    
    private final short cents;
    
    private final int multiplier;
    
    private final long allCents;
    
    private final Currency currencyID;
    
    /**
     * Tells whether or not the constructors for this class support a particular 
     * currency. Pseudocurrencies, such as precious metals, are not supported. 
     * Historical currencies, on the other hand, are.
     * @param currency The currency to check. Examples: the euro (EUR), the 
     * Slovenian tolar (SIT), the Yugoslavian new dinar (YUM, 1994 &mdash; 
     * 2002), gold (XAU).
     * @return True if the currency has 0 or positive default fraction digits in 
     * the currency information file, false otherwise. In the examples, this is 
     * obviously true for the euro, and it's also true for the Slovenian tolar 
     * even though it was replaced by the euro, and true for the Yugoslavian new 
     * dinar even though it seems to not have been accepted much in Slovenia. 
     * But this is definitely false for gold.
     */
    public static boolean supports(Currency currency) {
        return currency.getDefaultFractionDigits() > -1;
    }
    
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
    public short getDivisions() {
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
    
    /**
     * Adds a money amount to this one. For example, let's say this amount is 
     * $100.00.
     * @param addend The amount to add to this one. For example, $28.20.
     * @return The sum of the two amounts. For example, $128.20.
     * @throws CurrencyMismatchException If <code>addend</code> is not of the 
     * same currency amount as this one (e.g., if this amount is in U.&nbsp;S. 
     * dollars and <code>addend</code> is in euros).
     */
    public MoneyAmount plus(MoneyAmount addend) {
        if (!this.currencyID.equals(addend.currencyID)) {
            String excMsg = "Currency conversion needed to add " 
                    + this.currencyID.getDisplayName() + " (" 
                    + this.currencyID.getCurrencyCode() + ") and " 
                    + addend.currencyID.getDisplayName() + " (" 
                    + addend.currencyID.getCurrencyCode() + ")";
            throw new CurrencyMismatchException(excMsg, this, addend);
        }
        return new MoneyAmount(currencyID, this.allCents + addend.allCents, 
                this.multiplier);
    }
    
    /**
     * Gives the additive inverse of this money amount. The additive inverse is 
     * the amount that added to this amount is zero. For the example, let's say 
     * this amount is &minus;119,35&euro;.
     * @return This amount negated. For example, 119,35&euro;. Note that 0 of 
     * any currency is its own additive inverse. This function may or may not 
     * return a fresh instance in that case.
     */
    public MoneyAmount negate() {
        return new MoneyAmount(this.currencyID, -this.allCents, 
                this.multiplier);
    }
    
    /**
     * Subtracts a money amount from this money amount. For the example, let's 
     * say this amount is $128.20.
     * @param subtrahend The amount to subtract. For example, $30.50.
     * @return The subtraction. For example, $97.70. Even if the 
     * <code>subtrahend</code> is 0, this function will most likely return a 
     * fresh new instance.
     */
    public MoneyAmount minus(MoneyAmount subtrahend) {
        return this.plus(subtrahend.negate());
    }
    
    /**
     * Multiplies this money amount by an integer. This operation will generally 
     * be more reliable than multiplying by a floating point number, even if 
     * that floating point number is arithmetically equal to an integer.
     * @param multiplicand The number to multiply by. For example, 12.
     * @return The money amount multiplied by <code>multiplicand</code>. For 
     * example, if this money amount is 73,55&euro;, multiplied by 12 the result 
     * would be 882,60&euro;.
     */
    public MoneyAmount times(int multiplicand) {
        return new MoneyAmount(this.currencyID, allCents * multiplicand, 
                this.multiplier);
    }
    
    /**
     * Multiplies this money amount by a floating point number. Keep in mind 
     * that due to the vagaries of floating point, there may be occasional 
     * rounding errors.
     * @param multiplicand The number to multiply by. For example, 9.75.
     * @return The money amount multiplied by <code>multiplicand</code>, subject 
     * to loss of precision since divisions smaller than the currency's default 
     * subdivision might be ignored. For example, if this amount is $10.25, 
     * multiplied by 9.75 the result would be $99.94, not $99.9375.
     */
    public MoneyAmount times(double multiplicand) {
        long fullAmountInCents = (long) Math.ceil(multiplicand * this.allCents);
        return new MoneyAmount(this.currencyID, fullAmountInCents, 
                this.multiplier);
    }
    
    // TODO: Write tests for this
    public MoneyAmount divides(int divisor) {
        return this;
    }
    
    /**
     * Compares this money amount to another. For the examples, suppose this 
     * money amount is 70,89&euro;.
     * @param other The amount to compare this amount to. Examples: 40,00&euro;; 
     * 70,89&euro;; 70,90&euro;.
     * @return A negative integer, most likely &minus;1, if this amount is less 
     * than {@code other}; 0 if this amount is equal to {@code other}; a 
     * positive integer, most likely 1, if this amount is greater than {@code 
     * other}. In the examples, &minus;1 for 40,00&euro;; 0 for 70,89&euro;; 1 
     * for 70,90&euro;.
     */
    @Override
    public int compareTo(MoneyAmount other) {
        if (!this.currencyID.equals(other.currencyID)) {
            throw new CurrencyMismatchException(this, other);
        }
        return Long.compare(this.allCents, other.allCents);
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
    
    /**
     * Auxiliary constructor. If the cents or mills are 0 or not applicable, 
     * they can simply be omitted.
     * @param units How many units of the currency. For example, 20 for $20.00 
     * or &yen;20.
     * @param currency The currency. Examples: United States dollar (USD), 
     * Japanese yen (JPY).
     * @throws IllegalArgumentException If {@code currency} is a pseudocurrency 
     * like gold (XAU).
     * @throws NullPointerException If {@code currency} is null.
     */
    public MoneyAmount(long units, Currency currency) {
        this(units, currency, (short) 0);
    }

    /**
     * Primary constructor. Divisions of the unit (like cents or mills) must be 
     * specified. For the examples, let's say this amount is $37.99.
     * @param units The number of units. For example, 37.
     * @param currency The currency. For example, United States dollars (USD).
     * @param divisions The number of divisions of the unit. For example, 99.
     * @throws IllegalArgumentException If {@code currency} is a pseudocurrency 
     * like gold (XAU).
     * @throws NullPointerException If {@code currency} is null.
     */
    public MoneyAmount(long units, Currency currency, short divisions) {
        if (currency == null) {
            throw new NullPointerException("Currency should not be null");
        }
        if (currency.getDefaultFractionDigits() < 0) {
            String excMsg = "Pseudocurrency " + currency.getDisplayName() + " (" 
                    + currency.getCurrencyCode() + " is not valid";
            throw new IllegalArgumentException(excMsg);
        }
        this.multiplier = calculateMultiplier(currency);
        if (divisions > this.multiplier) {
            long overflowUnits = divisions / this.multiplier;
            units += overflowUnits;
            divisions -= overflowUnits * this.multiplier;
        }
        this.singles = units;
        this.cents = divisions;
        this.currencyID = currency;
        this.allCents = this.singles * this.multiplier + this.cents;
    }

    private MoneyAmount(Currency currency, long fullAmountInCents, 
            int verifiedMultiplier) {
        this.multiplier = verifiedMultiplier;
        this.singles = fullAmountInCents / this.multiplier;
        this.cents = (short) (fullAmountInCents - this.singles 
                * this.multiplier);
        this.currencyID = currency;
        this.allCents = this.singles * this.multiplier + this.cents;
    }    
}
