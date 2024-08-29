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
 * a new quote needs to be obtained.
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
     * Auxiliary constructor.
     * @param currencies The pair of currencies. For example, from United States 
     * dollars (USD) to euros (EUR).
     * @param rate The rate of conversion for one unit of the From currency to 
     * the To currency. For example, 0.9, meaning that one U.&nbsp;S. dollar 
     * converts to 90 cents of an euro.
     * @throws NullPointerException If {@code currencies} is null.
     */
    public ConversionRateQuote(CurrencyPair currencies, double rate) {
        if (currencies == null) {
            String excMsg = "Currency pair should not be null";
            throw new NullPointerException(excMsg);
        }
        if (Double.isNaN(rate) || rate == Double.NEGATIVE_INFINITY 
                || rate == Double.POSITIVE_INFINITY) {
            String excMsg = "Rate should not be " + rate;
            throw new IllegalArgumentException(excMsg);
        }
        this.pair = currencies;
        this.conversionRate = rate;
        this.fetchDate = LocalDateTime.now();
    }
    
    /**
     * Primary constructor.
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
