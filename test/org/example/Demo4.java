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
import currency.MoneyAmount;

import java.util.Currency;
import java.util.Locale;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Demonstrates the benefits of writing your own class to represent money 
 * amounts. But it also demonstrates the pitfalls, and also the duplication of 
 * effort that can arise.
 * @author Alonso del Arte
 */
public class Demo4 {
    
    private static final Currency DOLLARS = Currency.getInstance(Locale.US); 
    
    // TODO: Demo writing your own class
    @Test
    public void testAddMichiganSalesTax() {
        MoneyAmount price = new MoneyAmount(199, DOLLARS, (short) 99);
        double salesTaxRate = 0.06;
        MoneyAmount expected = new MoneyAmount(211, DOLLARS, (short) 99);
        MoneyAmount actual = price.plus(price.times(salesTaxRate));
        assertEquals(actual, expected);
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
