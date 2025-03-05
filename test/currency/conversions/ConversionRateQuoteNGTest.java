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
package currency.conversions;

import currency.CurrencyChooser;
import currency.CurrencyPair;
import static currency.MoneyAmountNGTest.provideNull;
import static currency.conversions.ExchangeRateProviderNGTest.RANDOM;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

import static org.testframe.api.Asserters.assertInRange;
import static org.testframe.api.Asserters.assertMinimum;
import static org.testframe.api.Asserters.assertThrows;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the ConversionRateQuote class. Note that the conversion rates used 
 * for these tests are for testing purposes only, and if they bear any relation 
 * to actual currency exchange rates, this is strictly a coincidence.
 * @author Alonso del Arte
 */
public class ConversionRateQuoteNGTest {
    
    @Test
    public void testPlaceholder() {
        fail("WRITE TESTS PERTAINING TO AUX CONSTRUCTOR");
    }
    
    @Test
    public void testGetCurrencies() {
        System.out.println("getCurrencies");
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair expected = new CurrencyPair(from, to);
        double rate = 0.5 + RANDOM.nextDouble();
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
        double expected = 0.5 + RANDOM.nextDouble();
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
        double rate = 0.5 + RANDOM.nextDouble();
        fail("CHANGE HOW expected IS CHOSEN");
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
        double rate = 0.5 + RANDOM.nextDouble();
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
        double rate = 0.5 + RANDOM.nextDouble();
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
        double rate = 0.5 + RANDOM.nextDouble();
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
        double rate = 0.5 + RANDOM.nextDouble();
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
        double rate = 0.5 + RANDOM.nextDouble();
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
        double rateA = 0.5 + RANDOM.nextDouble();
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
        double rate = 0.5 + RANDOM.nextDouble();
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
    public void testHashCode() {
        System.out.println("hashCode");
        int quartets = RANDOM.nextInt(16) + 4;
        int initialCapacity = 4 * quartets;
        Set<ConversionRateQuote> quotes = new HashSet<>(initialCapacity);
        Set<Integer> hashes = new HashSet<>(initialCapacity);
        LocalDateTime date = LocalDateTime.now();
        for (int i = 0; i < initialCapacity; i++) {
            Currency from = CurrencyChooser.chooseCurrency();
            Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
            CurrencyPair currencies = new CurrencyPair(from, to);
            double rate = 0.5 + RANDOM.nextDouble();
            date = date.minusMinutes(i);
            ConversionRateQuote quote1 = new ConversionRateQuote(currencies, 
                    rate, date);
            quotes.add(quote1);
            hashes.add(quote1.hashCode());
            ConversionRateQuote quote2 
                    = new ConversionRateQuote(currencies.flip(), rate, date);
            quotes.add(quote2);
            hashes.add(quote2.hashCode());
            ConversionRateQuote quote3 = new ConversionRateQuote(currencies, 
                    1.0 / rate, date);
            quotes.add(quote3);
            hashes.add(quote3.hashCode());
            ConversionRateQuote quote4 = new ConversionRateQuote(currencies, 
                    rate, date.minusHours(i));
            quotes.add(quote4);
            hashes.add(quote4.hashCode());
        }
        int numberOfQuotes = quotes.size();
        int minimum = 3 * numberOfQuotes / 5;
        int actual = hashes.size();
        String msg = "Given " + numberOfQuotes 
                + " quotes, there should be at least " + minimum 
                + " distinct hash codes, got " + actual + " distinct";
        assertMinimum(minimum, actual, msg);
        System.out.println(msg);
    }
    
    @Test
    public void testToString() {
        System.out.println("toString");
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double rate = 1.0 + RANDOM.nextDouble();
        LocalDateTime date = LocalDateTime.now()
                .minusMinutes(RANDOM.nextInt(60));
        ConversionRateQuote instance = new ConversionRateQuote(currencies, rate, 
                date);
        String expected = currencies.toString() + " at " + rate + " as of " 
                + date.toString();
        String actual = instance.toString();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testInvert() {
        System.out.println("invert");
        Currency from = CurrencyChooser.chooseCurrency();
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double rate = 1.0 + RANDOM.nextDouble();
        LocalDateTime date = LocalDateTime.now();
        ConversionRateQuote instance = new ConversionRateQuote(currencies, 
                rate, date);
        CurrencyPair flippedPair = currencies.flip();
        double invertedRate = 1.0 / rate;
        ConversionRateQuote expected = new ConversionRateQuote(flippedPair, 
                invertedRate, date);
        ConversionRateQuote actual = instance.invert();
        assertEquals(actual, expected);
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
