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

import static currency.CurrencyChooser.RANDOM;
import static currency.MoneyAmountNGTest.provideNull;

import java.time.LocalDateTime;
import java.util.Currency;

import static org.testframe.api.Asserters.assertInRange;
import static org.testframe.api.Asserters.assertThrows;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the ConversionRateQuote class.
 * @author Alonso del Arte
 */
public class ConversionRateQuoteNGTest {
    
    @Test
    public void testGetCurrencies() {
        System.out.println("getCurrencies");
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair expected = new CurrencyPair(from, to);
        double rate = RANDOM.nextDouble();
        LocalDateTime date = LocalDateTime.now();
        ConversionRateQuote instance = new ConversionRateQuote(expected, rate, 
                date);
        CurrencyPair actual = instance.getCurrencies();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testGetRate() {
        System.out.println("getRate");
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double expected = RANDOM.nextDouble();
        LocalDateTime date = LocalDateTime.now();
        ConversionRateQuote instance = new ConversionRateQuote(currencies, 
                expected, date);
        double actual = instance.getRate();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testGetDate() {
        System.out.println("getDate");
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double rate = RANDOM.nextDouble();
        LocalDateTime expected = LocalDateTime.now();
        ConversionRateQuote instance = new ConversionRateQuote(currencies, rate, 
                expected);
        LocalDateTime actual = instance.getDate();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testReferentialEquality() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double rate = RANDOM.nextDouble();
        LocalDateTime date = LocalDateTime.now();
        ConversionRateQuote quote = new ConversionRateQuote(currencies, rate, 
                date);
        assertEquals(quote, quote);
    }
    
    @Test
    public void testNotEqualsNull() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double rate = RANDOM.nextDouble();
        LocalDateTime date = LocalDateTime.now();
        ConversionRateQuote quote = new ConversionRateQuote(currencies, rate, 
                date);
        Object obj = provideNull();
        String msg = quote.toString() + " should not equal null";
        assert !quote.equals(obj) : msg;
    }
    
    @Test
    public void testNotEqualsDiffClass() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double rate = RANDOM.nextDouble();
        LocalDateTime date = LocalDateTime.now();
        ConversionRateQuote quote = new ConversionRateQuote(currencies, rate, 
                date);
        Object[] objects = {this, from, to, currencies, date};
        Object obj = objects[RANDOM.nextInt(objects.length)];
        String msg = quote.toString() + " should not equal " + obj.toString();
        assert !quote.equals(obj) : msg;
    }
    
    @Test
    public void testNotEqualsQuoteForDiffPair() {
        Currency fromA = CurrencyChooser.chooseCurrency();
        Currency toA = CurrencyChooser.chooseCurrencyOtherThan(fromA);
        CurrencyPair pairA = new CurrencyPair(fromA, toA);
        Currency fromB = CurrencyChooser.chooseCurrencyOtherThan(fromA);
        Currency toB = CurrencyChooser.chooseCurrencyOtherThan(toA);
        CurrencyPair pairB = new CurrencyPair(fromB, toB);
        double rate = RANDOM.nextDouble();
        LocalDateTime date = LocalDateTime.now();
        ConversionRateQuote quoteA = new ConversionRateQuote(pairA, rate, date);
        ConversionRateQuote quoteB = new ConversionRateQuote(pairB, rate, date);
        String message = "Quote for " + pairA.toString() 
                + " should not equal quote for " + pairB.toString();
        assertNotEquals(quoteA, quoteB, message);
    }
    
    @Test
    public void testEquals() {
        System.out.println("equals");
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double rate = RANDOM.nextDouble();
        LocalDateTime date = LocalDateTime.now();
        ConversionRateQuote someQuote = new ConversionRateQuote(currencies, 
                rate, date);
        ConversionRateQuote sameQuote = new ConversionRateQuote(currencies, 
                rate, date);
        String message = "Quote for " + currencies.toString() + " at a rate of " 
                + rate + " on " + date.toString() 
                + " should match other quote with those same parameters";
        assertEquals(sameQuote, someQuote, message);
    }
    
    @Test
    public void testNotEqualsDiffRate() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double rateA = RANDOM.nextDouble();
        double rateB = Double.longBitsToDouble(Double.doubleToLongBits(rateA) 
                + 1);
        LocalDateTime date = LocalDateTime.now();
        ConversionRateQuote quoteA = new ConversionRateQuote(currencies, rateA, 
                date);
        ConversionRateQuote quoteB = new ConversionRateQuote(currencies, rateB, 
                date);
        String message = "Quote for " + currencies.toString() + " at a rate of " 
                + rateA + " on " + date.toString() 
                + " should not match quote for same currencies at " + rateB;
        assertNotEquals(quoteA, quoteB, message);
    }
    
    @Test
    public void testNotEqualsDiffDate() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double rate = RANDOM.nextDouble();
        LocalDateTime dateA = LocalDateTime.now();
        LocalDateTime dateB = dateA.minusMinutes(RANDOM.nextInt(60) + 1);
        ConversionRateQuote quoteA = new ConversionRateQuote(currencies, rate, 
                dateA);
        ConversionRateQuote quoteB = new ConversionRateQuote(currencies, rate, 
                dateB);
        String message = "Quote for " + currencies.toString() + " at a rate of " 
                + rate + " on " + dateA.toString() 
                + " should not match quote for same currencies at same rate on " 
                + dateB.toString();
        assertNotEquals(quoteA, quoteB, message);
    }
    
    @Test
    public void testAuxiliaryConstructorFillsInCurrentDateTime() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double rate = 1.0 + RANDOM.nextDouble();
        ConversionRateQuote instance = new ConversionRateQuote(currencies, 
                rate);
        LocalDateTime expected = LocalDateTime.now();
        int minutes = 5;
        LocalDateTime minimum = expected.minusMinutes(minutes);
        LocalDateTime maximum = expected.plusMinutes(minutes);
        LocalDateTime actual = instance.getDate();
        assertInRange(minimum, actual, maximum);
    }
    
    @Test
    public void testConstructorRejectsNullCurrencyPair() {
        double rate = RANDOM.nextDouble();
        LocalDateTime date = LocalDateTime.now();
        String msg = "Using null currency pair should've caused NPE";
        Throwable t = assertThrows(() -> {
            ConversionRateQuote instance = new ConversionRateQuote(null, rate, 
                    date);
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
    public void testAuxConstructorRejectsNullCurrencyPair() {
        double rate = RANDOM.nextDouble();
        String msg = "Using null currency pair should've caused NPE";
        Throwable t = assertThrows(() -> {
            ConversionRateQuote instance = new ConversionRateQuote(null, rate);
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
    public void testConstructorRejectsNegativeInfinityRate() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double rate = Double.NEGATIVE_INFINITY;
        LocalDateTime date = LocalDateTime.now();
        String msg = "Using " + rate + " for rate should've caused exception";
        Throwable t = assertThrows(() -> {
            ConversionRateQuote instance = new ConversionRateQuote(currencies, 
                    rate, date);
            System.out.println(msg + ", not created instance " 
                    + instance.getClass().getName() + '@' 
                    + Integer.toHexString(System.identityHashCode(instance)));
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
    
    @Test
    public void testAuxConstructorRejectsNegativeInfinityRate() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double rate = Double.NEGATIVE_INFINITY;
        String msg = "Using " + rate + " for rate should've caused exception";
        Throwable t = assertThrows(() -> {
            ConversionRateQuote instance = new ConversionRateQuote(currencies, 
                    rate);
            System.out.println(msg + ", not created instance " 
                    + instance.getClass().getName() + '@' 
                    + Integer.toHexString(System.identityHashCode(instance)));
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
    
    @Test
    public void testConstructorRejectsPositiveInfinityRate() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double rate = Double.POSITIVE_INFINITY;
        LocalDateTime date = LocalDateTime.now();
        String msg = "Using " + rate + " for rate should've caused exception";
        Throwable t = assertThrows(() -> {
            ConversionRateQuote instance = new ConversionRateQuote(currencies, 
                    rate, date);
            System.out.println(msg + ", not created instance " 
                    + instance.getClass().getName() + '@' 
                    + Integer.toHexString(System.identityHashCode(instance)));
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
    
    @Test
    public void testAuxConstructorRejectsPositiveInfinityRate() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double rate = Double.POSITIVE_INFINITY;
        String msg = "Using " + rate + " for rate should've caused exception";
        Throwable t = assertThrows(() -> {
            ConversionRateQuote instance = new ConversionRateQuote(currencies, 
                    rate);
            System.out.println(msg + ", not created instance " 
                    + instance.getClass().getName() + '@' 
                    + Integer.toHexString(System.identityHashCode(instance)));
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
    
    @Test
    public void testConstructorRejectsNaNRate() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double rate = Double.NaN;
        LocalDateTime date = LocalDateTime.now();
        String msg = "Using " + rate + " for rate should've caused exception";
        Throwable t = assertThrows(() -> {
            ConversionRateQuote instance = new ConversionRateQuote(currencies, 
                    rate, date);
            System.out.println(msg + ", not created instance " 
                    + instance.getClass().getName() + '@' 
                    + Integer.toHexString(System.identityHashCode(instance)));
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
    
    @Test
    public void testAuxConstructorRejectsNaNRate() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double rate = Double.NaN;
        String msg = "Using " + rate + " for rate should've caused exception";
        Throwable t = assertThrows(() -> {
            ConversionRateQuote instance = new ConversionRateQuote(currencies, 
                    rate);
            System.out.println(msg + ", not created instance " 
                    + instance.getClass().getName() + '@' 
                    + Integer.toHexString(System.identityHashCode(instance)));
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
    
    @Test
    public void testConstructorRejectsNullDate() {
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double rate = RANDOM.nextDouble();
        String msg = "Using null date should've caused NPE";
        Throwable t = assertThrows(() -> {
            ConversionRateQuote instance = new ConversionRateQuote(currencies, 
                    rate, null);
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
