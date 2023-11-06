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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.testng.Assert.*;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyPropsLister class.
 * @author Alonso del Arte
 */
public class CurrencyPropsListerNGTest {
    
    private static final PrintStream USUAL_OUT = System.out;
    
    private PrintStream interceptor;
    
    private ByteArrayOutputStream stream;
    
    private void rerouteOut() {
        System.out.println("About to reroute usual output stream");
        this.stream = new ByteArrayOutputStream();
        this.interceptor = new PrintStream(stream);
        System.setOut(this.interceptor);
    }
    
    private void restoreOut() {
        try {
            this.stream.close();
            this.interceptor.close();
            System.setOut(USUAL_OUT);
            System.out.println("Restored usual output stream");
        } catch (IOException ioe) {
            System.err.println("Had problem restoring usual output stream");
            System.err.println("\"" + ioe.getMessage() + "\"");
        }
    }
    
    /**
     * Test of the printCurrencyInfo procedure, of the CurrencyPropsLister 
     * class.
     */
    @Test
    public void testPrintCurrencyInfo() {
        System.out.println("printCurrencyInfo");
        Currency currency = CurrencyChooser.chooseCurrency();
        System.out.println("Chose " + currency.getDisplayName() + " (" 
                + currency.getCurrencyCode() + ") for this test");
        String expected = currency.getDisplayName() 
                + "   Symbol: " + currency.getSymbol() + "   ISO 4217: " 
                + currency.getCurrencyCode() + "Number code: " 
                + currency.getNumericCode() + "   Default fraction digits: " 
                + currency.getDefaultFractionDigits();
        this.rerouteOut();
        CurrencyPropsLister.printCurrencyInfo(currency);
        String actual = this.stream.toString().replace("\n", "")
                .replace("\r", "");
        this.restoreOut();
        assertEquals(actual, expected);
    }

    /**
     * Test of main method, of class CurrencyPropsLister.
     */
//    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        CurrencyPropsLister.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    

}
