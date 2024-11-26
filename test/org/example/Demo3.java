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

import currency.conversions.ExchangeRateProvider;
import currency.conversions.MannysCurrencyConverterAPIAccess;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
        BigDecimal price = new BigDecimal("199.99");
        BigDecimal salesTaxRate = new BigDecimal("0.06");
        BigDecimal expected = new BigDecimal("211.99");
        BigDecimal actual = price.add(price.multiply(salesTaxRate));
        assertEquals(actual, expected);
    }
    
    @Test
    public void demoFormattingEffort() {
        BigDecimal price = new BigDecimal("199.99");
        BigDecimal salesTaxRate = new BigDecimal("0.06");
        BigDecimal priceWithTax = price.add(price.multiply(salesTaxRate));
        DecimalFormat formatter = new DecimalFormat("'$'0.00");
        String expected = "$211.99";
        String actual = formatter.format(priceWithTax);
        assertEquals(actual, expected);
    }
    
    @Test
    public void testAddDollarsAndEuros() {
        String fiveHundred = "500.00";
        BigDecimal fiveHundredDollars = new BigDecimal(fiveHundred);
        BigDecimal fiveHundredEuros = new BigDecimal(fiveHundred);
        Currency dollar = Currency.getInstance(Locale.US);
        Currency euro = Currency.getInstance("EUR");
        ExchangeRateProvider rateProvider 
                = new MannysCurrencyConverterAPIAccess();
        BigDecimal expectedA = fiveHundredDollars.add(fiveHundredEuros
                .multiply(new BigDecimal(rateProvider.getRate(euro, 
                        dollar))));
        BigDecimal expectedB = fiveHundredEuros.add(fiveHundredDollars
                .multiply(new BigDecimal(rateProvider.getRate(dollar, 
                        euro))));
        BigDecimal actual = fiveHundredDollars.add(fiveHundredEuros);
        String msg = "$" + fiveHundredDollars.toPlainString() + " + \u20AC" 
                + fiveHundredEuros.toPlainString() + " should be either $" 
                + expectedA.toPlainString() + " or \u20AC" 
                + expectedB.toPlainString() + ", got ?" + actual;
        assert expectedA.equals(actual) || expectedB.equals(actual) : msg;
    }
    
}
