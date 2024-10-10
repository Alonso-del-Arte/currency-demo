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
package org.example;

import currency.ExchangeRateProvider;
import currency.MannysCurrencyConverterAPIAccess;

import java.util.Currency;
import java.util.Locale;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Demonstrates the follies of using <code>long</code> primitives to represent 
 * money amounts. On the one hand, there are the following advantages to using 
 * <code>long</code>:
 * <ul>
 * <li>It's a primitive type already defined for the Java Virtual Machine 
 * (JVM).</li>
 * <li>Only one zero value: "positive" zero.</li>
 * <li>No values wasted on NaNs nor infinities.</li>
 * <li>The arithmetic operators, such as <code>+</code> and <code>*</code>, are 
 * already defined in the Java Language Specification (JLS).</li>
 * <li>Comparison operators like <code>&lt;</code> and <code>&gt;=</code> are 
 * also already defined in the JLS.</li>
 * </ul>
 * <p>But there are the following disadvantages:</p>
 * <ul>
 * <li>Loss of precision when working with numbers that can't be represented 
 * accurately in binary, such as <sup>1</sup>&frasl;<sub>3</sub>.</li>
 * <li>Problematic to change the unit of precision, e.g., cents to mills.</li>
 * <li>Possible values limited to the range {@link java.lang.Long#MIN_VALUE} to 
 * {@link java.lang.Long#MAX_VALUE}.</li>
 * <li>{@link java.lang.Long#MIN_VALUE} is its own two's complement.</li>
 * <li>No information about currency.</li>
 * <li>Special effort needed to format values like $1.00 or 53,70&#x20AC;.</li>
 * </ul>
 * @author Alonso del Arte
 */
public class Demo2 {
    
    private static final char DOLLAR_SYMBOL = '$';
    
    private static final char EURO_SYMBOL = '\u20AC';
    
    private static String format(char symbol, long amount) {
        StringBuilder builder = new StringBuilder(Long.toString(amount));
        builder.insert(0, symbol);
        builder.insert(builder.length() - 2, '.');
        return builder.toString();
    }
    
    @Test
    public void testAddMichiganSalesTax() {
        long price = 19999;
        double salesTaxRate = 0.06;
        long expectedNumber = 21199;
        long actualNumber = price + ((long) (price * salesTaxRate));
        String expected = format(DOLLAR_SYMBOL, expectedNumber);
        String actual = format(DOLLAR_SYMBOL, actualNumber);
        String msg = "Calculating Michigan 6% sales tax for price " 
                + format(DOLLAR_SYMBOL, price);
        assertEquals(actual, expected, msg);
    }
    
    @Test
    public void testAddDollarsAndEuros() {
        long dollars = 50000;
        long euros = 50000;
        Currency dollar = Currency.getInstance(Locale.US);
        Currency euro = Currency.getInstance("EUR");
        ExchangeRateProvider rateProvider 
                = new MannysCurrencyConverterAPIAccess();
        long expectedA = dollars + ((long) (euros 
                * rateProvider.getRate(euro, dollar)));
        long expectedB = euros + ((long) (dollars 
                * rateProvider.getRate(dollar, euro)));
        long actual = dollars + euros;
        String msg = format(DOLLAR_SYMBOL, dollars) + " + " 
                + format(EURO_SYMBOL, euros) + " should be either " 
                + format(DOLLAR_SYMBOL, expectedA) + " or " 
                + format(EURO_SYMBOL, expectedB) + ", got " 
                + format('?', actual);
        System.out.println(msg);
        assert expectedA == actual || expectedB == actual : msg;
    }
    
}
