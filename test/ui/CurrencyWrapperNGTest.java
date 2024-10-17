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
package ui;

import currency.CurrencyChooser;
import static currency.MoneyAmountNGTest.provideNull;

import java.util.Currency;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyWrapper class.
 * @author Alonso del Arte
 */
public class CurrencyWrapperNGTest {
    
    @Test
    public void testGetWrappedCurrency() {
        System.out.println("getWrappedCurrency");
        Currency expected = CurrencyChooser.chooseCurrency();
        CurrencyWrapper instance = new CurrencyWrapper(expected);
        Currency actual = instance.getWrappedCurrency();
        String message = "Inquiring currency wrapped by " + instance.toString();
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testToString() {
        System.out.println("toString");
        Currency currency = CurrencyChooser.chooseCurrency(
                (cur) -> !cur.getSymbol().equals(cur.getCurrencyCode())
        );
        CurrencyWrapper instance = new CurrencyWrapper(currency);
        String expected = currency.getCurrencyCode() + " \u2014 " 
                + currency.getSymbol() + " \u2014 " + currency.getDisplayName() 
                + " (" + currency.getNumericCodeAsString() + ")";
        String actual = instance.toString();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testToStringIfSymbolInLocaleEqualToCurrencyCode() {
        System.out.println("toString");
        Currency currency = CurrencyChooser.chooseCurrency(
                (cur) -> cur.getSymbol().equals(cur.getCurrencyCode())
        );
        CurrencyWrapper instance = new CurrencyWrapper(currency);
        String expected = currency.getCurrencyCode() + " \u2014 " 
                + currency.getDisplayName() + " (" 
                + currency.getNumericCodeAsString() + ")";
        String actual = instance.toString();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testReferentialEquality() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyWrapper instance = new CurrencyWrapper(currency);
        String msg = instance.toString() + " should be equal to itself";
        assert instance.equals(instance) : msg;
    }
    
    @Test
    public void testNotEqualsNull() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyWrapper instance = new CurrencyWrapper(currency);
        String msg = instance.toString() + " should not equal null";
        Object obj = provideNull();
        assert !instance.equals(obj) : msg;
    }
    
    @Test
    public void testNotEqualsDiffClass() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyWrapper instance = new CurrencyWrapper(currency);
        String msg = instance.toString() + " of class " 
                + instance.getClass().getName() + " should not equal " 
                + currency.toString() + " of class " 
                + currency.getClass().getName();
        Object obj = currency;
        assert !instance.equals(obj) : msg;
    }
    
    @Test
    public void testNotEqualsDiffWrappedCurrency() {
        Currency currencyA = CurrencyChooser.chooseCurrency();
        Currency currencyB = CurrencyChooser.chooseCurrencyOtherThan(currencyA);
        CurrencyWrapper wrapperA = new CurrencyWrapper(currencyA);
        CurrencyWrapper wrapperB = new CurrencyWrapper(currencyB);
        String msg = wrapperA.toString() + " should not equal " 
                + wrapperB.toString();
        assert !wrapperA.equals(wrapperB) : msg;
    }
    
    @Test
    public void testEquals() {
        System.out.println("equals");
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyWrapper expected = new CurrencyWrapper(currency);
        CurrencyWrapper actual = new CurrencyWrapper(currency);
        assertEquals(actual, expected);
    }
    
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyWrapper instance = new CurrencyWrapper(currency);
        int currencyHashCode = currency.hashCode();
        int expected = ((~currencyHashCode) << 1) + 1;
        int actual = instance.hashCode();
        String message = "Reckoning hash code of wrapper " + instance.toString() 
                + " which wraps Currency instance with hash code " 
                + currencyHashCode;
        assertEquals(actual, expected, message);
    }
    
}
