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
import static currency.CurrencyChooser.chooseCurrencyOtherThan;
import static currency.CurrencyChooser.RANDOM;

import java.util.Currency;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyMismatchException class.
 * @author Alonso del Arte
 */
public class CurrencyMismatchExceptionNGTest {
    
    private static MoneyAmount chooseAmountA() {
        return new MoneyAmount(RANDOM.nextInt(1024), chooseCurrency(2), 
                (short) RANDOM.nextInt(100));
    }
    
    private static MoneyAmount chooseAmountB(MoneyAmount amtA) {
        Currency currencyA = amtA.getCurrency();
        return new MoneyAmount(RANDOM.nextInt(1024), 
                chooseCurrencyOtherThan(currencyA));
    }
    
    /**
     * Test of the getMessage function, of the CurrencyMismatchException class.
     */
    @Test
    public void testGetMessage() {
        System.out.println("getMessage");
        String expected = "FOR TESTING PURPOSES";
        MoneyAmount amtA = chooseAmountA();
        MoneyAmount amtB = chooseAmountB(amtA);
        Throwable exc = new CurrencyMismatchException(expected, amtA, amtB);
        String actual = exc.getMessage();
        assertEquals(actual, expected);
    }
    
    /**
     * Another test of the getMessage function, of the CurrencyMismatchException 
     * class.
     */
    @Test
    public void testDefaultMessageConstructorFillsInMessageWithAmounts() {
        MoneyAmount amtA = chooseAmountA();
        MoneyAmount amtB = chooseAmountB(amtA);
        Throwable exc = new CurrencyMismatchException(amtA, amtB);
        String amtAStr = amtA.toString();
        String amtBStr = amtB.toString();
        String excMsg = exc.getMessage();
        System.out.println("\"" + excMsg + "\"");
        String msg = "Message should include amounts " + amtAStr + " and " 
                + amtBStr;
        assert excMsg.contains(amtAStr) : msg;
        assert excMsg.contains(amtBStr) : msg;
    }
    
    /**
     * Test of the getAmountA function, of the CurrencyMismatchException class.
     */
    @Test
    public void testGetAmountA() {
        System.out.println("getAmountA");
        MoneyAmount expected = chooseAmountA();
        MoneyAmount amtB = chooseAmountB(expected);
        CurrencyMismatchException exc = new CurrencyMismatchException(expected, 
                amtB);
        MoneyAmount actual = exc.getAmountA();
        assertEquals(actual, expected);
    }

    /**
     * Test of getAmountB method, of class CurrencyMismatchException.
     */
    @Test
    public void testGetAmountB() {
        System.out.println("getAmountB");
        CurrencyMismatchException instance = null;
        MoneyAmount expResult = null;
//        MoneyAmount result = instance.getAmountB();
//        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
