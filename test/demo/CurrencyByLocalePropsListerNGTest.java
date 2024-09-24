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
package demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;
import java.util.Random;

import static org.testframe.api.Asserters.assertPrintOut;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyByLocalePropsLister class.
 * @author Alonso del Arte
 */
public class CurrencyByLocalePropsListerNGTest {
    
    private static final Locale[] LOCALES = Locale.getAvailableLocales();
    
    private static final int NUMBER_OF_LOCALES = LOCALES.length;
    
    private static final Random RANDOM = new Random();
    
    private static Locale chooseLocale(boolean hasAssociatedCurrency) {
        Locale locale = null;
        boolean flag = !hasAssociatedCurrency;
        while (flag ^ hasAssociatedCurrency) {
            locale = LOCALES[RANDOM.nextInt(NUMBER_OF_LOCALES)];
            try {
                Currency currency = Currency.getInstance(locale);
                System.out.println("Chose locale " + locale.getDisplayName() 
                        + " which is associated with currency " 
                        + currency.getDisplayName() + " (" 
                        + currency.getCurrencyCode() + ")");
                flag = true;
            } catch (IllegalArgumentException iae) {
                System.err.println(iae.getMessage());
                System.out.println("Chose locale " + locale.getDisplayName() 
                        + " which is not associated with any currency");
                flag = false;
            }
        }
        return locale;
    }
    
    @Test
    public void testPrintCurrencyInfo() {
        System.out.println("printCurrencyInfo");
        Locale locale = chooseLocale(true);
        Currency currency = Currency.getInstance(locale);
        String currencyDisplayName = currency.getDisplayName();
        String genSymbol = "Symbol: " + currency.getSymbol();
        String locSpecSymbol = "Symbol for locale " + locale.getDisplayName() 
                + ": " + currency.getSymbol(locale);
        String iso4217Code = "ISO 4217 letter code: " 
                + currency.getCurrencyCode();
        String numberCode = "IS 4217 number code: " 
                + currency.getNumericCodeAsString();
        String fractDigits = "Default fraction digits: " 
                + currency.getDefaultFractionDigits();
        String[] expected = {currencyDisplayName, genSymbol, locSpecSymbol, 
            iso4217Code, numberCode, fractDigits};
        String msg = "Print-out should include " + Arrays.toString(expected);
        assertPrintOut((s -> s.contains(currencyDisplayName) 
                && s.contains(genSymbol) && s.contains(locSpecSymbol) 
                && s.contains(iso4217Code) && s.contains(numberCode) 
                && s.contains(fractDigits)), () -> {
                    CurrencyByLocalePropsLister.printCurrencyInfo(locale);
        }, msg);
    }
    
    @Test
    public void testPrintCurrencyInfoAcknowledgesUnassociatedLocale() {
        Locale locale = chooseLocale(false);
        String localeDisplayName = locale.getDisplayName();
        String msg = "Print-out should include " + localeDisplayName 
                + ", which is not associated with any currency";
        assertPrintOut((s -> s.contains(localeDisplayName)), () -> {
            CurrencyByLocalePropsLister.printCurrencyInfo(locale);
        }, msg);
    }
    
    /**
     * Test of main method, of class CurrencyByLocalePropsLister.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        CurrencyByLocalePropsLister.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
