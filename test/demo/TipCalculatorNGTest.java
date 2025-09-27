/*
 * Copyright (C) 2025 Alonso del Arte
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
package demo;

import static demo.CurrencyByLocalePropsListerNGTest.chooseLocale;

import java.util.Currency;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the TipCalculator class.
 * @author Alonso del Arte
 */
public class TipCalculatorNGTest {
    
    @Test
    public void testGetCurrency() {
        System.out.println("getCurrency");
        Locale defaultLocale = Locale.getDefault();
        boolean hasAssociatedCurrency = true;
        Locale locale = chooseLocale(hasAssociatedCurrency);
        Locale.setDefault(locale);
        Currency expected = Currency.getInstance(locale);
        TipCalculator instance = new TipCalculator();
        Currency actual = instance.getCurrency();
        String message = "Locale " + locale.getDisplayName() 
                + " is associated with currency " + expected.getDisplayName() 
                + " (" + expected.getCurrencyCode() + ")";
        Locale.setDefault(defaultLocale);
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testConstructorSetsDefaultCloseOperation() {
        JFrame instance = new TipCalculator();
        int expected = WindowConstants.EXIT_ON_CLOSE;
        int actual = instance.getDefaultCloseOperation();
        String message = "Constructor should set EXIT_ON_CLOSE";
        assertEquals(actual, expected, message);
    }
    
}
