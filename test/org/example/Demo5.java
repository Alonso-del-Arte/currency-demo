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

import currency.conversions.CurrencyConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Locale;

import org.joda.money.CurrencyMismatchException;
import org.joda.money.Money;

import static org.testframe.api.Asserters.assertThrows;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Demonstrates the benefits of using the <code>Money</code> class from Joda 
 * Money to represent money amounts. Like creating your own class from scratch, 
 * using using Joda Money avoids the pitfalls of using floating point primitives 
 * or <code>BigDecimal</code>, but the work of designing the necessary classes 
 * and interfaces has already been done for you.
 * @author Alonso del Arte
 */
public class Demo5 {
    
    @Test
    public void testAddMichiganSalesTax() {
        Money price = Money.parse("USD 199.99");
        BigDecimal salesTaxRate = new BigDecimal("0.06");
        Money expected = Money.parse("USD 211.99");
        Money actual = price.plus(price.multipliedBy(salesTaxRate, 
                RoundingMode.CEILING));
        assertEquals(actual, expected);
    }
    
    @Test
    public void testAddDollarsAndEuros() {
        Money dollars = Money.parse("USD 500.00");
        Money euros = Money.parse("EUR 500.00");
        String msg = "Trying to add " + dollars.toString() + " and " 
                + euros.toString() + " should cause an exception";
        Throwable t = assertThrows(() -> {
            Money sum = dollars.plus(euros);
            System.out.println(dollars.toString() + " plus " + euros.toString() 
                    + " is said to be " + sum.toString());
        }, CurrencyMismatchException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
    
}
