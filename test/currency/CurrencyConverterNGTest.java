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

import java.util.Currency;
import java.util.Locale;

import static org.testframe.api.Asserters.assertInRange;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyConverter class.
 * @author Alonso del Arte
 */
public class CurrencyConverterNGTest {
    
    private static final double TEST_DELTA = 0.001;
    
    private static final Currency U_S_DOLLARS = Currency.getInstance(Locale.US);
    
    private static final Currency EAST_CARIBBEAN_DOLLARS 
            = Currency.getInstance("XCD");
    
    @Test
    public void testGetRateNoConversionNeeded() {
        Currency currency = CurrencyChooser.chooseCurrency();
        double expected = 1.0;
        double actual = Double.parseDouble(CurrencyConverter.getRate(currency, 
                currency));
        String iso4217Code = currency.getCurrencyCode();
        String msg = "No conversion needed for " + iso4217Code + " to " 
                + iso4217Code;
        assertEquals(actual, expected, TEST_DELTA, msg);
    }
    
    @Test
    public void testGetRateForUSDollarsToEastCaribbeanDollars() {
        double expected = 2.702;
        double actual = Double.parseDouble(CurrencyConverter
                .getRate(U_S_DOLLARS, EAST_CARIBBEAN_DOLLARS));
        assertEquals(actual, expected, TEST_DELTA);
    }
    
    @Test
    public void testGetRateForEastCaribbeanDollarsToUSDollars() {
        double expected = 1.0 / 2.7;
        double actual = Double.parseDouble(CurrencyConverter
                .getRate(EAST_CARIBBEAN_DOLLARS, U_S_DOLLARS));
        assertEquals(actual, expected, TEST_DELTA);
    }
    
    /**
     * Test of the getRate function, of the CurrencyConverter class.
     */
    @Test
    public void testGetRate() {
        System.out.println("getRate");
        Currency firstTarget = CurrencyChooser
                .chooseCurrencyOtherThan(U_S_DOLLARS);
        String dollarsDisplayName = U_S_DOLLARS.getDisplayName();
        String dollarsISO4217Code = U_S_DOLLARS.getCurrencyCode();
        String firstTargetDisplayName = firstTarget.getDisplayName();
        String firstTargetISO4217Code = firstTarget.getCurrencyCode();
        System.out.println("Inquiring rate of conversion from " 
                + dollarsDisplayName + " (" + dollarsISO4217Code + ") to " 
                + firstTargetDisplayName + " (" + firstTargetISO4217Code + ")");
        double fromDollars = Double.parseDouble(CurrencyConverter
                .getRate(U_S_DOLLARS, firstTarget));
        double toDollars = Double.parseDouble(CurrencyConverter
                .getRate(firstTarget, U_S_DOLLARS));
        double expected = 1.0;
        double actual = fromDollars * toDollars;
        String message = "Rate of conversion from " + dollarsDisplayName + " (" 
                + dollarsISO4217Code + ") to " + firstTargetDisplayName + " (" 
                + firstTargetISO4217Code + ") is said to be " + fromDollars 
                + ", and vice-versa is said to be " + toDollars;
        System.out.println(message);
        assertEquals(actual, expected, TEST_DELTA, message);
    }
    
    @Test
    public void testConvertButNoConvertNeeded() {
        int units = RANDOM.nextInt(Short.MAX_VALUE);
        Currency currency = CurrencyChooser.chooseCurrency();
        MoneyAmount expected = new MoneyAmount(units, currency);
        MoneyAmount actual = CurrencyConverter.convert(expected, currency);
        String message = "Converting " + expected.toString() + " to " 
                + currency.getDisplayName() + " should not need conversion";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testConvertUSDollarsToEastCaribbeanDollars() {
        int units = RANDOM.nextInt(Short.MAX_VALUE) + Byte.MAX_VALUE;
        short divisions = (short) RANDOM.nextInt(100);
        MoneyAmount source = new MoneyAmount(units, U_S_DOLLARS, divisions);
        int expectedUnits = (int) Math.floor(2.7 * units);
        int marginOfError = expectedUnits / 320;
        MoneyAmount minimum = new MoneyAmount(expectedUnits - marginOfError, 
                EAST_CARIBBEAN_DOLLARS);
        MoneyAmount maximum = new MoneyAmount(expectedUnits + marginOfError, 
                EAST_CARIBBEAN_DOLLARS);
        MoneyAmount expected = new MoneyAmount(expectedUnits, 
                EAST_CARIBBEAN_DOLLARS);
        MoneyAmount actual = CurrencyConverter.convert(source, 
                EAST_CARIBBEAN_DOLLARS);
        String xcdDisplayName = EAST_CARIBBEAN_DOLLARS.getDisplayName();
        String message = "Conversion of " + source.toString() + " to " 
                + xcdDisplayName + " should give amount of that currency, got " 
                + actual.toString();
        assertEquals(actual.getCurrency(), EAST_CARIBBEAN_DOLLARS, message);
        String msg = "Converting " + source.toString() + " to " + xcdDisplayName 
                + " should give roughly " + expected.toString();
        assertInRange(minimum, actual, maximum, msg);
        System.out.println(msg + ", got " + actual.toString());
    }
    
    @Test
    public void testConvertEastCaribbeanDollarsToUSDollars() {
        int units = RANDOM.nextInt(Short.MAX_VALUE) + Byte.MAX_VALUE;
        short divisions = (short) RANDOM.nextInt(100);
        MoneyAmount source = new MoneyAmount(units, EAST_CARIBBEAN_DOLLARS, 
                divisions);
        int expectedUnits = (int) Math.floor(0.37 * units);
        int marginOfError = expectedUnits / 320;
        MoneyAmount minimum = new MoneyAmount(expectedUnits - marginOfError, 
                U_S_DOLLARS);
        MoneyAmount maximum = new MoneyAmount(expectedUnits + marginOfError, 
                U_S_DOLLARS);
        if (minimum.equals(maximum)) {
            MoneyAmount adjustment = new MoneyAmount(2, U_S_DOLLARS);
            maximum = maximum.plus(adjustment);
        }
        MoneyAmount expected = new MoneyAmount(expectedUnits, 
                U_S_DOLLARS);
        MoneyAmount actual = CurrencyConverter.convert(source, U_S_DOLLARS);
        String usdDisplayName = U_S_DOLLARS.getDisplayName();
        String message = "Conversion of " + source.toString() + " to " 
                + usdDisplayName + " should give amount of that currency, got " 
                + actual.toString();
        assertEquals(actual.getCurrency(), U_S_DOLLARS, message);
        String msg = "Converting " + source.toString() + " to " + usdDisplayName 
                + " should give roughly " + expected.toString();
        assertInRange(minimum, actual, maximum, msg);
        System.out.println(msg + ", got " + actual.toString());
    }
    
    @Test
    public void testConvert() {
        System.out.println("convert");
        Currency currency = CurrencyChooser.chooseCurrency(
                (cur) -> !cur.getSymbol().equals(cur.getCurrencyCode())
        );
        int units = RANDOM.nextInt(Short.MAX_VALUE) + Byte.MAX_VALUE;
        short divisions = 0;
        int exponent = currency.getDefaultFractionDigits();
        if (exponent > 0) {
            int bound = 1;
            while (exponent > 0) {
                bound *= 10;
                exponent--;
            }
            divisions = (short) RANDOM.nextInt(bound);
        }
        MoneyAmount minimum = new MoneyAmount(units - 10, currency);
        MoneyAmount maximum = new MoneyAmount(units + 10, currency);
        Currency target = CurrencyChooser.chooseCurrencyOtherThan(currency);
        MoneyAmount source = new MoneyAmount(units, currency, divisions);
        System.out.println("Inquiring to convert " + source.toString() + " to " 
                + target.getDisplayName() + " (" + target.getCurrencyCode() 
                + ")");
        MoneyAmount intermediate = CurrencyConverter.convert(source, target);
        assertEquals(intermediate.getCurrency(), target);
        MoneyAmount actual = CurrencyConverter.convert(intermediate, currency);
        String msg = source.toString() + " is said to convert to " 
                + intermediate.toString() 
                + ", and that's said to convert back to " + actual.toString();
        assertInRange(minimum, actual, maximum, msg);
        System.out.println(msg);
    }
    
}
