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
import static currency.MoneyAmountNGTest.provideNull;

import java.util.Currency;
import java.util.HashSet;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Set;

import static org.testframe.api.Asserters.assertThrows;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyPair class.
 * @author Alonso del Arte
 */
public class CurrencyPairNGTest {
    
    private static final Locale[] LOCALES = Locale.getAvailableLocales();
    
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
     * Test of the getToCurrency function, of the CurrencyPair class.
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
    public void testFlip() {
        System.out.println("flip");
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair instance = new CurrencyPair(from, to);
        CurrencyPair expected = new CurrencyPair(to, from);
        CurrencyPair actual = instance.flip();
        String message = "Expected from " + from.getDisplayName() + " to " 
                + to.getDisplayName() + " to flip to from " 
                + to.getDisplayName() + " to " + from.getDisplayName();
        assertEquals(actual, expected, message);
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
    public void testToDisplayString() {
        System.out.println("toDisplayString");
        CurrencyPair instance = CurrencyChooser.choosePair();
        Currency from = instance.getFromCurrency();
        Currency to = instance.getToCurrency();
        String msgPart = "Display name for " + instance.toString() 
                + " in locale ";
        for (Locale locale : LOCALES) {
            String fromName = from.getDisplayName(locale);
            String toName = to.getDisplayName(locale);
            ResourceBundle res = ResourceBundle.getBundle("i18n.uiLabels");
            String dirWord = ' ' + res.getString("directionToWord") + ' ';
            String inclSpStr = res.getString("includeSpaces");
            boolean inclSpaces = Boolean.parseBoolean(inclSpStr);
            String connector = (inclSpaces) ? ' ' + dirWord + ' ' : dirWord;
            String expected = fromName + connector + toName;
            String actual = instance.toDisplayString(locale);
            String message = msgPart + locale.getDisplayName();
            assertEquals(actual, expected, message);
        }
    }
    
    @Test
    public void testReferentialEquality() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair somePair = new CurrencyPair(from, to);
        assertEquals(somePair, somePair);
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

    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        int initialCapacity = RANDOM.nextInt(64) + 16;
        Set<CurrencyPair> pairs = new HashSet<>(initialCapacity);
        Set<Integer> hashes = new HashSet<>(initialCapacity);
        for (int i = 0; i < initialCapacity; i++) {
            Currency from = CurrencyChooser.chooseCurrency();
            Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
            CurrencyPair pair = new CurrencyPair(from, to);
            pairs.add(pair);
            hashes.add(pair.hashCode());
        }
        int expected = pairs.size();
        String message = "Set of " + expected 
                + " currency pairs should have as many hashes";
        int actual = hashes.size();
        assertEquals(actual, expected, message);
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
