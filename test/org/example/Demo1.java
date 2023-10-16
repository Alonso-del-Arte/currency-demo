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

import currency.CurrencyConverter;

import java.util.Currency;
import java.util.Locale;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Demonstrates the follies of using <code>double</code> primitives to represent 
 * money amounts. On the one hand, there are the following advantages to using 
 * <code>double</code>:
 * <ul>
 * <li>It's a primitive type already defined for the Java Virtual Machine 
 * (JVM).</li>
 * <li>The arithmetic operators, such as <code>+</code> and <code>*</code>, are 
 * already defined in the Java Language Specification (JLS).</li>
 * <li>Comparison operators like <code>&lt;</code> and <code>&gt;=</code> are 
 * also already defined in the JLS.</li>
 * </ul>
 * <p>But there are the following disadvantages:</p>
 * <ul>
 * <li>Special values {@link java.lang.Double#NEGATIVE_INFINITY}, {@link 
 * java.lang.Double#POSITIVE_INFINITY} and {@link java.lang.Double#NaN} 
 * (canonical) are of no use for financial calculations.</li>
 * <li>Trillions of quiet and signaling NaN values are of no use for financial 
 * calculations and are anyway unavailable without the use of "native 
 * methods" (they otherwise get "collapsed" to canonical NaN by the JVM).</li>
 * <li>Trillions of subnormal values that are also of no use for financial 
 * calculations.</li>
 * <li>Negative zero could potentially cause misunderstandings in regards to the 
 * application of fees to an account.</li>
 * <li>Frequent loss of precision, especially with repeated addition or 
 * subtraction.</li>
 * <li>The floating point data types were designed for scientific calculations, 
 * not financial calculations.</li>
 * <li>No information about currency.</li>
 * <li>Special effort needed to format values like $1.00 or 53,70&#x20AC;.</li>
 * </ul>
 * @author Alonso del Arte
 */
public class Demo1 {
    
    @Test
    public void testAddMichiganSalesTax() {
        double price = 199.99;
        double salesTaxRate = 0.06;
        double expected = 211.99;
        double actual = price + (price * salesTaxRate);
        assertEquals(actual, expected, 0.0);
    }
    
    @Test
    public void testAddDollarsAndEuros() {
        double dollars = 500.00;
        double euros = 500.00;
        Currency dollar = Currency.getInstance(Locale.US);
        Currency euro = Currency.getInstance("EUR");
        double expectedA = dollars + (euros 
                * Double.parseDouble(CurrencyConverter.getRate(euro, dollar)));
        double expectedB = euros + (dollars 
                * Double.parseDouble(CurrencyConverter.getRate(dollar, euro)));
        double actual = dollars + euros;
        String msg = "$" + dollars + " + \u20AC" + euros + " should be either $" 
                + expectedA + " or \u20AC" + expectedB + ", got ?" + actual;
        System.out.println(msg);
        fail("HAVEN'T FINISHED WRITING CURRENCY CONVERTER YET");
        assert expectedA == actual || expectedB == actual : msg;
    }
    
}
