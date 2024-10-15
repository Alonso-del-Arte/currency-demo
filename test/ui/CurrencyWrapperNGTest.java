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

import java.util.Currency;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyWrapper class.
 * @author Alonso del Arte
 */
public class CurrencyWrapperNGTest {
    
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
    
//    @Test
    public void testNotYetReferentialEquality() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyWrapper instance = new CurrencyWrapper(currency);
    }
    
}
