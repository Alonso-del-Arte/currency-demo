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

import java.time.LocalDateTime;

/**
 * Provides a dated currency conversion rate quote. It will be up to the caller 
 * to determine whether the quote is still good or if it has become outdated and 
 * a new quote needs to be obtained. A timestamp is provided in addition to the 
 * currencies queried and their exchange rate. Objects of this class contain no 
 * other information about the quote.
 * @author Alonso del Arte
 */
public class ConversionRateQuote {
    
    private final CurrencyPair pair;
    
    private final double conversionRate;
    
    private final LocalDateTime fetchDate;
    
    /**
     * Retrieves the currencies this quote was initialized with.
     * @return The currency pair passed to the constructor. For example, United 
     * States dollars (USD) to euros (EUR).
     */
    public CurrencyPair getCurrencies() {
        return this.pair;
    }
    
    /**
     * Retrieves the rate this quote was initialized with.
     * @return The rate passed to the constructor. For example, 0.9.
     */
    public double getRate() {
        return this.conversionRate;
    }
    
    /**
     * Retrieves the date and time this quote was initialized with.
     * @return The date and time passed to the constructor. For example, 5:35 
     * p.m. on August 26, 2024.
     */
    public LocalDateTime getDate() {
        return this.fetchDate;
    }
    
    /**
     * Calculates the conversion rate in the opposite direction based off of 
     * this quote. In some cases, this might be preferable to making a new API 
     * call. For the example, suppose that this quote is United States dollars 
     * (USD) to euros (EUR) at 0.94369 on November 18, 2024 at 7:29 p.m.
     * @return A conversion rate quote calculated from this quote. The From 
     * currency becomes the To currency and vice-versa, the rate is the 
     * reciprocal of this quote's rate, the timestamp is the same, since 
     * presumably the API would have given this rate if so queried at the exact 
     * same time. In the example, this would be EUR to USD at 1.0596700187561594 
     * on November 18, 2024 at 7:29 p.m. To construct this example, I did make 
     * API calls in both directions at roughly the same time. The API that I 
     * used gave me 1.059714 for EUR to USD.
     */
    public ConversionRateQuote invert() {
        CurrencyPair currencies = this.pair.flip();
        double rate = 1.0 / this.conversionRate;
        return new ConversionRateQuote(currencies, rate, this.fetchDate);
    }
    
    /**
     * Determines whether or not this conversion rate quote is equal to some 
     * other object. For the examples, suppose this quote is for U.&nbsp;S. 
     * dollars (USD) to euros (EUR) at a rate of 0.89755 fetched on September 
     * 30, 2024 at 7:15 p.m. local time.
     * @param obj The object to compare. Examples: USD to EUR at 0.9 on 
     * September 30, 2024 at 7:15 p.m.; USD to EUR at 0.89755 on September 30, 
     * 2024 at 7:15 p.m.; USD to EUR at 0.89755 on September 30, 2024 at 7:18 
     * p.m.; USD to Swiss francs (CHF) at a rate of 0.844955 on September 30, 
     * 2024 at 7:15 p.m.; EUR to USD at 1.114168 on September 30, 2024 at 7:15 
     * p.m.; the date September 30, 2024; and null.
     * @return True if {@code obj} is another conversion rate quote for the same 
     * pair of currencies in the same direction at the same rate on the same 
     * date and time, false in all other cases. Note that there is no variance 
     * allowed for either the rate or the date and time. A quote of USD to EUR 
     * at 0.9 is thus considered different than a quote with a rate of 0.89755, 
     * even though that small difference is unlikely to have any practical 
     * impact in a real world situation, so this function returns false for the 
     * first example. The second example should return true, unless there was a 
     * variance of a few milliseconds not shown here. In the USD to CHF example, 
     * this function would return false without checking whether or not the rate 
     * and date match. The same is also true in the EUR to USD example even 
     * though it's the same two currencies, because the direction differs. And 
     * it also returns false for the last two examples.
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
        ConversionRateQuote other = ((ConversionRateQuote) obj);
        if (!this.pair.equals(other.pair)) {
            return false;
        }
        if (this.conversionRate != other.conversionRate) {
            return false;
        }
        return this.fetchDate.equals(other.fetchDate);
    }

    /**
     * Gives a hash code for this quote. It is mathematically impossible to 
     * guarantee unique hash codes for all possible quotes. The most we can hope 
     * for is that the hash codes are unique during a given session. The exact 
     * formula may be subject to change in the future.
     * @return A hash code based on the currency pair, rate and timestamp. For  
     * example, for a quote of United States dollars (USD) to euros (EUR) of 
     * 0.90344 obtained on October 1, 2024 at 5:18 p.m., the hash code might be 
     * 220015088. A quote for the same currencies at the same rate obtained on 
     * the same date at 5:19 p.m. might have a hash code of &minus;1561856979.
     */
    @Override
    public int hashCode() {
        int hash = this.pair.hashCode();
        hash += Double.doubleToLongBits(this.conversionRate);
        return hash + this.fetchDate.hashCode();
    }
    
    @Override
    public String toString() {
        return this.pair.toString() + " at " + this.conversionRate + " as of " 
                + this.fetchDate.toString();
    }
    
    /**
     * Auxiliary constructor. Use this constructor when the quote date is very 
     * close to the current time. Otherwise, use the {@link 
     * #ConversionRateQuote(currency.CurrencyPair, double, 
     * java.time.LocalDateTime) three-parameter constructor}.
     * @param currencies The pair of currencies. For example, from United States 
     * dollars (USD) to euros (EUR).
     * @param rate The rate of conversion for one unit of the From currency to 
     * the To currency. For example, 0.9, meaning that one U.&nbsp;S. dollar 
     * converts to 90 cents of an euro.
     * @throws IllegalArgumentException If {@code rate} is NaN or 
     * &plusmn;&infin;.
     * @throws NullPointerException If {@code currencies} is null.
     */
    public ConversionRateQuote(CurrencyPair currencies, double rate) {
        this(currencies, rate, LocalDateTime.now());
    }
    
    /**
     * Primary constructor. Use this constructor when it is necessary to specify 
     * a quote date that is very different from the current time. Otherwise, use 
     * the {@link #ConversionRateQuote(currency.CurrencyPair, double) 
     * two-parameter constructor}.
     * @param currencies The pair of currencies. For example, from United States 
     * dollars (USD) to euros (EUR).
     * @param rate The rate of conversion for one unit of the From currency to 
     * the To currency. For example, 0.9, meaning that one U.&nbsp;S. dollar 
     * converts to 90 cents of an euro.
     * @param date The date and time the currency quote was obtained. For 
     * example, 5:35 p.m. on August 26, 2024.
     * @throws IllegalArgumentException If {@code rate} is NaN or 
     * &plusmn;&infin;.
     * @throws NullPointerException If {@code currencies} or {@code date} or 
     * both are null.
     */
    public ConversionRateQuote(CurrencyPair currencies, double rate, 
            LocalDateTime date) {
        if (currencies == null || date == null) {
            String excMsg = "Currency pair, date, should not be null";
            throw new NullPointerException(excMsg);
        }
        if (!Double.isFinite(rate)) {
            String excMsg = "Rate " + rate + " is not valid";
            throw new IllegalArgumentException(excMsg);
        }
        this.pair = currencies;
        this.conversionRate = rate;
        this.fetchDate = date;
    }
    
}
