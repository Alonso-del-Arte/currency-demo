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
package currency;

import static currency.CurrencyChooser.RANDOM;

import java.util.Currency;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the UnsupportedCurrencyException class.
 * @author Alonso del Arte
 */
public class UnsupportedCurrencyExceptionNGTest {
    
    private static final String DEFAULT_MESSAGE = "For testing purposes only";
    
    @Test
    public void testGetCurrency() {
        System.out.println("getCurrency");
        Currency expected = CurrencyChooser.chooseCurrency();
        UnsupportedCurrencyException instance 
                = new UnsupportedCurrencyException(expected);
        Currency actual = instance.getCurrency();
        String message = "Expecting to retrieve " + expected.getDisplayName();
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testGetCurrencyFromTwoParamConstructedInstance() {
        Currency expected = CurrencyChooser.chooseCurrency();
        UnsupportedCurrencyException instance 
                = new UnsupportedCurrencyException(expected, DEFAULT_MESSAGE);
        Currency actual = instance.getCurrency();
        String message = "Expecting to retrieve " + expected.getDisplayName();
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testGetMessage() {
        System.out.println("getMessage");
        Currency currency = CurrencyChooser.chooseCurrency();
        String expected = "Sample testing message " + RANDOM.nextInt();
        UnsupportedCurrencyException instance 
                = new UnsupportedCurrencyException(currency, expected);
        String actual = instance.getMessage();
        assertEquals(actual, expected);
    }
    
}
