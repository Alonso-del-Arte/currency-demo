/*
 * Copyright (C) 2023 Alonso del Arte
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

import static currency.CurrencyChooser.chooseCurrency;
import static currency.CurrencyChooser.RANDOM;

import java.util.Currency;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the MoneyAmount class. Note that these tests are written for 
 * Locale.US. Some of the tests for toString() might fail because they expect 
 * String instances like "$499.89" rather than "USD499.89".
 * @author Alonso del Arte
 */
public class MoneyAmountNGTest {
    
    private static final Currency DINARS 
            = Currency.getInstance(Locale.forLanguageTag("ar-LY"));
    
    static final Currency DOLLARS = Currency.getInstance(Locale.US);
    
    private static final Currency EUROS = Currency.getInstance("EUR");
    
    private static final Currency YEN = Currency.getInstance(Locale.JAPAN);

    @Test
    public void testGetUnits() {
        System.out.println("getUnits");
        int expected = RANDOM.nextInt(32768) - 16384;
        MoneyAmount amount = new MoneyAmount(expected, chooseCurrency());
        long actual = amount.getUnits();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testGetFullAmountInCents() {
        System.out.println("getFullAmountInCents");
        int dollars = RANDOM.nextInt(524288);
        short cents = (short) RANDOM.nextInt(100);
        MoneyAmount amount = new MoneyAmount(dollars, DOLLARS, cents);
        int expected = dollars * 100 + cents;
        long actual = amount.getFullAmountInCents();
        assertEquals(actual, expected);
    }

    @Test
    public void testGetFullAmountInCentsYen() {
        int expected = RANDOM.nextInt(524288);
        MoneyAmount amount = new MoneyAmount(expected, YEN);
        long actual = amount.getFullAmountInCents();
        assertEquals(actual, expected);
    }

    @Test
    public void testGetFullAmountInCentsDinars() {
        int dinars = RANDOM.nextInt(524288);
        short darahim = (short) RANDOM.nextInt(1000);
        MoneyAmount amount = new MoneyAmount(dinars, DINARS, darahim);
        int expected = dinars * 1000 + darahim;
        long actual = amount.getFullAmountInCents();
        assertEquals(actual, expected);
    }

    @Test
    public void testGetDivisions() {
        System.out.println("getDivisions");
        short expected = (short) RANDOM.nextInt(100);
        MoneyAmount amount = new MoneyAmount(0, EUROS, expected);
        long actual = amount.getDivisions();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testGetCurrency() {
        System.out.println("getCurrency");
        Currency expected = chooseCurrency();
        MoneyAmount amount = new MoneyAmount(0, expected);
        Currency actual = amount.getCurrency();
        assertEquals(actual, expected);
    }
        
    @Test
    public void testToStringZeroToNineCents() {
        for (short cents = 0; cents < 10; cents++) {
            MoneyAmount amount = new MoneyAmount(0, DOLLARS, cents);
            String expected = "$0.0" + cents;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }

    @Test
    public void testToStringTenToNinetyNineCents() {
        for (short cents = 10; cents < 100; cents++) {
            MoneyAmount amount = new MoneyAmount(0, DOLLARS, cents);
            String expected = "$0." + cents;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testToStringDollarsPlusZeroToNineCents() {
        int dollarQty = RANDOM.nextInt(1000) + 1;
        String part = "$" + dollarQty + ".0";
        for (short cents = 0; cents < 10; cents++) {
            MoneyAmount amount = new MoneyAmount(dollarQty, DOLLARS, cents);
            String expected = part + cents;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testToStringDollarsPlusTenToNinetyCentsCents() {
        int dollarQty = RANDOM.nextInt(1000) + 1;
        String part = "$" + dollarQty + '.';
        for (short cents = 10; cents < 100; cents++) {
            MoneyAmount amount = new MoneyAmount(dollarQty, DOLLARS, cents);
            String expected = part + cents;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testToStringNegativeDollarAmountPlusZeroToNineCents() {
        int dollarQty = RANDOM.nextInt(1000) + 1;
        String part = "-$" + dollarQty + ".0";
        for (short cents = 0; cents < 9; cents++) {
            MoneyAmount amount = new MoneyAmount(-dollarQty, DOLLARS, cents);
            String expected = part + cents;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testToStringNegativeDollarAmountPlusTenToNinetyNineCents() {
        int dollarQty = RANDOM.nextInt(1000) + 1;
        String part = "-$" + dollarQty + '.';
        for (short cents = 10; cents < 100; cents++) {
            MoneyAmount amount = new MoneyAmount(-dollarQty, DOLLARS, cents);
            String expected = part + cents;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testToStringEurosZeroToNineCents() {
        for (short cents = 0; cents < 10; cents++) {
            MoneyAmount amount = new MoneyAmount(0, EUROS, cents);
            String expected = "\u20AC0.0" + cents;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }

    @Test
    public void testToStringEurosTenToNinetyNineCents() {
        for (short cents = 10; cents < 100; cents++) {
            MoneyAmount amount = new MoneyAmount(0, EUROS, cents);
            String expected = "\u20AC0." + cents;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testToStringEurosPlusZeroToNineCents() {
        int euroQty = RANDOM.nextInt(1000) + 1;
        String part = "\u20AC" + euroQty + ".0";
        for (short cents = 0; cents < 10; cents++) {
            MoneyAmount amount = new MoneyAmount(euroQty, EUROS, cents);
            String expected = part + cents;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testToStringEurosPlusTenToNinetyCentsCents() {
        int euroQty = RANDOM.nextInt(1000) + 1;
        String part = "\u20AC" + euroQty + '.';
        for (short cents = 10; cents < 100; cents++) {
            MoneyAmount amount = new MoneyAmount(euroQty, EUROS, cents);
            String expected = part + cents;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testToStringEurosNegativeEuroAmountPlusZeroToNineCents() {
        int euroQty = RANDOM.nextInt(1000) + 1;
        String part = "-\u20AC" + euroQty + ".0";
        for (short cents = 0; cents < 10; cents++) {
            MoneyAmount amount = new MoneyAmount(-euroQty, EUROS, cents);
            String expected = part + cents;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testToStringEurosNegativeEuroAmountPlusTenToNinetyNineCents() {
        int euroQty = RANDOM.nextInt(1000) + 1;
        String part = "-\u20AC" + euroQty + '.';
        for (short cents = 10; cents < 100; cents++) {
            MoneyAmount amount = new MoneyAmount(-euroQty, EUROS, cents);
            String expected = part + cents;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testToStringDinarsZeroToNineDarahim() {
        for (short darahim = 0; darahim < 10; darahim++) {
            MoneyAmount amount = new MoneyAmount(0, DINARS, darahim);
            String expected = "LYD0.00" + darahim;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }

    @Test
    public void testToStringDinarsTenToNinetyNineDarahim() {
        for (short darahim = 10; darahim < 100; darahim++) {
            MoneyAmount amount = new MoneyAmount(0, DINARS, darahim);
            String expected = "LYD0.0" + darahim;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testToStringDinars100To999Darahim() {
        for (short darahim = 100; darahim < 1000; darahim++) {
            MoneyAmount amount = new MoneyAmount(0, DINARS, darahim);
            String expected = "LYD0." + darahim;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testToStringDinarsPlusZeroToNineDarahim() {
        int dinarQty = RANDOM.nextInt(1000) + 1;
        String part = "LYD" + dinarQty + ".00";
        for (short darahim = 0; darahim < 10; darahim++) {
            MoneyAmount amount = new MoneyAmount(dinarQty, DINARS, darahim);
            String expected = part + darahim;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testToStringDinarsPlusTenToNinetyDarahimDarahim() {
        int dinarQty = RANDOM.nextInt(1000) + 1;
        String part = "LYD" + dinarQty + ".0";
        for (short darahim = 10; darahim < 100; darahim++) {
            MoneyAmount amount = new MoneyAmount(dinarQty, DINARS, darahim);
            String expected = part + darahim;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testToStringDinarsPlus100To999DarahimDarahim() {
        int dinarQty = RANDOM.nextInt(1000) + 1;
        String part = "LYD" + dinarQty + '.';
        for (short darahim = 100; darahim < 1000; darahim++) {
            MoneyAmount amount = new MoneyAmount(dinarQty, DINARS, darahim);
            String expected = part + darahim;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testToStringDinarsNegativeDinarAmountPlusZeroToNineDarahim() {
        int dinarQty = RANDOM.nextInt(1000) + 1;
        String part = "-LYD" + dinarQty + ".00";
        for (short darahim = 0; darahim < 10; darahim++) {
            MoneyAmount amount = new MoneyAmount(-dinarQty, DINARS, darahim);
            String expected = part + darahim;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testToStringDinarsNegativeDinarAmtPlusTenToNinetyNineDarahim() {
        int dinarQty = RANDOM.nextInt(1000) + 1;
        String part = "-LYD" + dinarQty + ".0";
        for (short darahim = 10; darahim < 100; darahim++) {
            MoneyAmount amount = new MoneyAmount(-dinarQty, DINARS, darahim);
            String expected = part + darahim;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testToString() {
        System.out.println("toString");
        int yenQty = RANDOM.nextInt(1048576);
        MoneyAmount amount = new MoneyAmount(yenQty, YEN);
        String expected = "\u00A5" + yenQty;
        String actual = amount.toString();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testToStringDinarsNegativeDinarAmountPlus100To999NineDarahim() {
        int dinarQty = RANDOM.nextInt(1000) + 1;
        String part = "-LYD" + dinarQty + '.';
        for (short darahim = 100; darahim < 1000; darahim++) {
            MoneyAmount amount = new MoneyAmount(-dinarQty, DINARS, darahim);
            String expected = part + darahim;
            String actual = amount.toString();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testReferentialEquality() {
        int units = RANDOM.nextInt(1048576);
        Currency currency = chooseCurrency();
        MoneyAmount amount = new MoneyAmount(units, currency);
        assertEquals(amount, amount);
    }
    
    private Object provideNull() {
        return null;
    }
    
    @Test
    public void testNotEqualsNull() {
        int units = RANDOM.nextInt(1048576);
        Currency currency = chooseCurrency();
        MoneyAmount amount = new MoneyAmount(units, currency);
        String msg = amount.toString() + " should not equal null";
        assert !amount.equals(this.provideNull()) : msg;
    }
    
    @Test
    public void testNotEqualsDiffClass() {
        int units = RANDOM.nextInt(1048576);
        Currency currency = chooseCurrency();
        MoneyAmount amount = new MoneyAmount(units, currency);
        MoneyAmount diffClassAmount = new MoneyAmount(units, currency) {
            
            @Override
            public String toString() {
                return this.getClass().getName() + "<" + super.toString() + ">";
            }
            
        };
        String msg = amount.toString() + " should not equal " 
                + diffClassAmount.toString();
        assertNotEquals(amount, diffClassAmount, msg);
    }

    @Test
    public void testNotEqualsDiffCurrency() {
        int units = RANDOM.nextInt(1048576);
        MoneyAmount amountA = new MoneyAmount(units, DOLLARS);
        MoneyAmount amountB = new MoneyAmount(units, EUROS);
        String msg = amountA.toString() + " should not equal " 
                + amountB.toString() + " regardless of exchange rate";
        assert !amountA.equals(amountB) : msg;
    }
    
    @Test
    public void testEquals() {
        System.out.println("equals");
        int units = RANDOM.nextInt(1048576);
        Currency currency = chooseCurrency();
        MoneyAmount someAmount = new MoneyAmount(units, currency);
        MoneyAmount sameAmount = new MoneyAmount(units, currency);
        assertEquals(someAmount, sameAmount);
    }
    
    @Test
    public void testNotEqualsDifferentEuroAmount() {
        int units = RANDOM.nextInt(1048576);
        MoneyAmount amountA = new MoneyAmount(units, EUROS);
        MoneyAmount amountB = new MoneyAmount(units + 1, EUROS);
        String msg = amountA.toString() + " should not equal " 
                + amountB.toString();
        assert !amountA.equals(amountB) : msg;
    }
    
    @Test
    public void testNotEqualsDifferentCentsAmount() {
        int dollarQty = RANDOM.nextInt(1000) + 1;
        for (short cents = 0; cents < 99; cents++) {
            MoneyAmount amountA = new MoneyAmount(dollarQty, DOLLARS, cents);
            MoneyAmount amountB = new MoneyAmount(dollarQty, DOLLARS, 
                    (short) (cents +  1));
        String msg = amountA.toString() + " should not equal " 
                + amountB.toString();
        assert !amountA.equals(amountB) : msg;
        }
    }
    
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        int capacity = RANDOM.nextInt(256) + 64;
        Set<MoneyAmount> amounts = new HashSet<>(capacity);
        Set<Integer> hashes = new HashSet<>(capacity);
        while (amounts.size() < capacity) {
            int units = RANDOM.nextInt();
            MoneyAmount amount = new MoneyAmount(units, chooseCurrency());
            MoneyAmount dinarAmount = new MoneyAmount(units, DINARS);
            MoneyAmount dollarAmount = new MoneyAmount(units, DOLLARS);
            MoneyAmount euroAmount = new MoneyAmount(units, EUROS);
            MoneyAmount yenAmount = new MoneyAmount(units, YEN);
            amounts.add(amount);
            amounts.add(dinarAmount);
            amounts.add(dollarAmount);
            amounts.add(euroAmount);
            amounts.add(yenAmount);
            hashes.add(amount.hashCode());
            hashes.add(dinarAmount.hashCode());
            hashes.add(dollarAmount.hashCode());
            hashes.add(euroAmount.hashCode());
            hashes.add(yenAmount.hashCode());
        }
        int expected = amounts.size();
        int actual = hashes.size();
        assertEquals(actual, expected);
    }
    
//    @Test
//    void testConstructorRejectsPseudoCurrencies() {
//        Set<Currency> currencies = Currency.getAvailableCurrencies();
//        for (Currency currency : currencies) {
//            int fractDigits = currency.getDefaultFractionDigits();
//            if (fractDigits < 0) {
//                Throwable t = assertThrows(IllegalArgumentException.class, 
//                        () -> {
//                    MoneyAmount badAmount = new MoneyAmount(0, currency);
//                    System.out.println("Instantiated amount " 
//                            + badAmount.toString() + " with currency "  
//                            + currency.getDisplayName() + " which has " 
//                            + fractDigits + " fractional digits");
//                });
//                String excMsg = t.getMessage();
//                assert excMsg != null : "Message should not be null";
//                String symbol = currency.getSymbol();
//                String msg = "Message should include symbol " + symbol 
//                        + " for pseudo-currency " + currency.getDisplayName();
//                assert excMsg.contains(symbol) : msg;
//            }
//        }
//    }
    
//    @Test
//    void testConstructorRejectsNullCurrency() {
//        int units = RANDOM.nextInt(1048576);
//        Throwable t = assertThrows(NullPointerException.class, () -> {
//            MoneyAmount amount = new MoneyAmount(units, null);
//            System.out.println("Should not have created " 
//                    + amount.getClass().getName() + '@' 
//                    + Integer.toString(amount.hashCode(), 16) + " for " + units 
//                    + " of null currency");
//        });
//        String excMsg = t.getMessage();
//        assert excMsg != null : "Message should not be null";
//    }

}
