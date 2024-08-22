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
package currency;

import java.util.Currency;

import static org.testframe.api.Asserters.assertThrows;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyPair class.
 * @author Alonso del Arte
 */
public class CurrencyPairNGTest {
    
    /**
     * Test of the getFromCurrency function, of the CurrencyPair class.
     */
    @Test
    public void testGetFromCurrency() {
        System.out.println("getFromCurrency");
        Currency expected = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(expected);
        CurrencyPair instance = new CurrencyPair(expected, to);
        Currency actual = instance.getFromCurrency();
        assertEquals(actual, expected);
    }

    /**
     * Test of getToCurrency method, of class CurrencyPair.
     */
    @Test
    public void testGetToCurrency() {
        System.out.println("getToCurrency");
        Currency from = CurrencyChooser.chooseCurrency();
        Currency expected = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair instance = new CurrencyPair(from, expected);
        Currency actual = instance.getToCurrency();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testConstructorRejectsNullFromCurrency() {
        Currency to = CurrencyChooser.chooseCurrency();
        String msg = "Currency pair with null From currency and " 
                + to.getDisplayName() + " (" + to.getCurrencyCode() 
                + ") should cause NPE";
        Throwable t = assertThrows(() -> {
            CurrencyPair instance = new CurrencyPair(null, to);
            System.out.println(msg + ", not created instance " 
                    + instance.getClass().getName() + '@' 
                    + Integer.toHexString(System.identityHashCode(instance)));
        }, NullPointerException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
    
}
