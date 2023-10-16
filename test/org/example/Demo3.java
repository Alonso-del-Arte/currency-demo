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

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Demonstrates the follies of using <code>BigDecimal</code> primitives to 
 * represent money amounts. On the one hand, there are the following advantages 
 * to using <code>BigDecimal</code>:
 * <ul>
 * <li>It's a reference type already defined in the Java Development Kit 
 * (JDK).</li>
 * <li>Only one zero value: "positive" zero.</li>
 * <li>No values wasted on NaNs nor infinities.</li>
 * <li>Range is limited only by available memory, so overflows and underflow are
 * highly unlikely in this use case.</li>
 * </ul>
 * <p>But there are the following disadvantages:</p>
 * <ul>
 * <li>The arithmetic operators, such as <code>+</code> and <code>*</code>, are 
 * not available. Instead, we must use {@link 
 * BigDecimal#add(java.math.BigDecimal) add()}, {@link 
 * BigDecimal#subtract(java.math.BigDecimal) subtract()}, etc.</li>
 * <li>Instead of the comparison operators, we must use {@link 
 * BigDecimal#compareTo(java.math.BigDecimal) compare()}</li>
 * <li></li>
 * <li>No information about currency.</li>
 * <li>Special effort needed to format values like $1.00 or 53,70&#x20AC;.</li>
 * </ul>
 * @author Alonso del Arte
 */
public class Demo3 {
    
    @Test
    public void testAddMichiganSalesTax() {
        fail("REWRITE");
        double price = 199.99;
        double salesTaxRate = 0.06;
        double expected = 211.99;
        double actual = price + (price * salesTaxRate);
        assertEquals(actual, expected, 0.0);
    }
    
    @Test
    public void testAddDollarsAndEuros() {
        fail("REWRITE");
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
