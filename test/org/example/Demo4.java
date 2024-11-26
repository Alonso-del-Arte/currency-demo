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
package org.example;

import currency.CurrencyMismatchException;
import currency.MoneyAmount;
import currency.conversions.CurrencyConverter;

import java.util.Currency;
import java.util.Locale;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

import static org.testframe.api.Asserters.assertThrows;

/**
 * Demonstrates the benefits of writing your own class to represent money 
 * amounts. But it also demonstrates the pitfalls, and also the duplication of 
 * effort that can arise.
 * @author Alonso del Arte
 */
public class Demo4 {
    
    private static final Currency DOLLARS = Currency.getInstance(Locale.US); 
    
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
        int units = 500;
        Currency euro = Currency.getInstance("EUR");
        MoneyAmount dollars = new MoneyAmount(units, DOLLARS);
        MoneyAmount euros = new MoneyAmount(units, euro);
        String dolStr = dollars.toString();
        String eurStr = euros.toString();
        String msg = "Trying to add " + dolStr + " to " + eurStr 
                + " should have caused an exception";
        CurrencyMismatchException cme = assertThrows(() -> {
            MoneyAmount badAmount = dollars.plus(euros);
            System.out.println(msg + ", not given " + badAmount);
        }, CurrencyMismatchException.class, msg);
        String excMsg = cme.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        String containsMsg = "Exception message should contain \'" + dolStr 
                + "\" and \"" + eurStr + "\"";
        assert excMsg.contains(dolStr) : containsMsg;
        assert excMsg.contains(eurStr) : containsMsg;
    }
    
}
