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
    public void testToString() {
        System.out.println("toString");
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair instance = new CurrencyPair(from, to);
        String expected = from.getCurrencyCode() + '_' + to.getCurrencyCode();
        String actual = instance.toString();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testReferentialEquality() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair somePair = new CurrencyPair(from, to);
        assertEquals(somePair, somePair);
    }
    
    private static Object provideNull() {
        return null;
    }
    
    @Test
    public void testNotEqualsNull() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair somePair = new CurrencyPair(from, to);
        Object obj = provideNull();
        String msg = "Pair " + somePair.toString() + " should not equal null";
        assert !somePair.equals(obj) : msg;
    }
    
    @Test
    public void testNotEqualsDiffClass() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair pair = new CurrencyPair(from, to);
        String msg = "Pair " + pair.toString() + " should not equal " 
                + from.toString() + " nor " + to.toString();
        assertNotEquals(from, pair, msg);
        assertNotEquals(to, pair, msg);
    }
    
    @Test
    public void testNotEqualsDiffFromCurrency() {
        Currency fromA = CurrencyChooser.chooseCurrency();
        Currency fromB = CurrencyChooser.chooseCurrencyOtherThan(fromA);
        Currency to = CurrencyChooser.chooseCurrency();
        CurrencyPair somePair = new CurrencyPair(fromA, to);
        CurrencyPair diffPair = new CurrencyPair(fromB, to);
        String message = somePair.toString() + " should not match " 
                + diffPair.toString();
        assertNotEquals(diffPair, somePair, message);
    }
    
    @Test
    public void testNotEqualsDiffToCurrency() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency toA = CurrencyChooser.chooseCurrency();
        Currency toB = CurrencyChooser.chooseCurrency();
        CurrencyPair somePair = new CurrencyPair(from, toA);
        CurrencyPair diffPair = new CurrencyPair(from, toB);
        String message = somePair.toString() + " should not match " 
                + diffPair.toString();
        assertNotEquals(diffPair, somePair, message);
    }
    
    @Test
    public void testEquals() {
        System.out.println("equals");
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair somePair = new CurrencyPair(from, to);
        CurrencyPair samePair = new CurrencyPair(from, to);
        assertEquals(samePair, somePair);
    }
    
    public void testHashCode() {
        fail("PLACEHOLDER FOR TEST");
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
    
    @Test
    public void testConstructorRejectsNullToCurrency() {
        Currency from = CurrencyChooser.chooseCurrency();
        String msg = "Currency pair with " + from.getDisplayName() + " (" 
                + from.getCurrencyCode() 
                + ") and null To currency should cause NPE";
        Throwable t = assertThrows(() -> {
            CurrencyPair instance = new CurrencyPair(from, null);
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
