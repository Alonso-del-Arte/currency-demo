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
package demo;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import currency.CurrencyChooser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import static org.testframe.api.Asserters.assertDoesNotThrow;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

import static org.testframe.api.Asserters.assertInRange;
import static org.testframe.api.Asserters.assertThrows;

/**
 * Tests of the CurrencyInfoJSONServer class.
 * @author Alonso del Arte
 */
public class CurrencyInfoJSONServerNGTest {
    
    private static final Random RANDOM 
            = new Random(System.currentTimeMillis() >> 4);
    
    private static final Locale DEFAULT_LOCALE = Locale.getDefault();
    
    private static final Locale[] LOCALES = Locale.getAvailableLocales();
    
    private static final int NUMBER_OF_LOCALES = LOCALES.length;
    
    private static final Set<Currency> CURRENCIES 
            = Currency.getAvailableCurrencies();
    
    @Test
    public void testDefaultHTTPPortConstant() {
        int expected = 8080;
        int actual = CurrencyInfoJSONServer.DEFAULT_HTTP_PORT;
        assertEquals(actual, expected);
    }
    
    @Test
    public void testDefaultClosingDelayConstant() {
        int minimum = 1;
        int maximum = 10;
        int actual = CurrencyInfoJSONServer.DEFAULT_CLOSING_DELAY;
        String msg = "Default closing delay should not be more than 10 seconds";
        assertInRange(minimum, actual, maximum, msg);
    }
    
    @Test
    public void testContentTypeSpecification() {
        String expected = "application/json; charset=" 
                + StandardCharsets.UTF_8.toString();
        String actual = CurrencyInfoJSONServer.CONTENT_TYPE_SPECIFICATION;
        assertEquals(actual, expected);
    }
    
    @Test
    public void testGetPort() {
        int expected = RANDOM.nextInt(8000, 8100);
        CurrencyInfoJSONServer instance = new CurrencyInfoJSONServer(expected, 
                DEFAULT_LOCALE);
        int actual = instance.getPort();
        assertEquals(actual, expected);
    }
    
    private static Locale chooseLocale() {
        int index = RANDOM.nextInt(NUMBER_OF_LOCALES);
        return LOCALES[index];
    }
    
    @Test
    public void testGetLocale() {
        System.out.println("getLocale");
        for (Locale expected : LOCALES) {
            CurrencyInfoJSONServer instance 
                    = new CurrencyInfoJSONServer(expected);
            Locale actual = instance.getLocale();
            assertEquals(actual, expected);
        }
    }
    
    private static char chooseUpperCaseLetter() {
        return (char) (RANDOM.nextInt(26) + 'A');
    }
    
    private static String chooseThreeUpperCaseLetters() {
        char[] letters = new char[3];
        for (int i = 0; i < 3; i++) {
            letters[i] = chooseUpperCaseLetter();
        }
        return new String(letters);
    }
    
    @Test
    public void testGetCurrencyInfoForUnrecognizedCurrencyCode() {
        Set<String> curCodes = CURRENCIES.stream().map(c -> c.getCurrencyCode())
                .collect(Collectors.toSet());
        String currencyCode = "USD";
        while (curCodes.contains(currencyCode)) {
            currencyCode = chooseThreeUpperCaseLetters();
        }
        String expected 
                = "{\"result\":\"error\",\"error-type\":\"unsupported-code\"}";
        String actual = CurrencyInfoJSONServer.getCurrencyInfo(currencyCode)
                .replace(" ", "")
                .replace("\n", "")
                .replace("\r", "");
        String message = "Currency code \"" + currencyCode 
                + "\" should not be recognized";
        assertEquals(actual, expected, message);
    }
    
    /**
     * Test of the getCurrencyInfo function, of the CurrencyInfoJSONServer 
     * class.
     */
    @Test
    public void testGetCurrencyInfo() {
        System.out.println("getCurrencyInfo");
        for (Currency currency : CURRENCIES) {
            String currencyCode = currency.getCurrencyCode();
            String expected = "{\"name\":\"" + currency.getDisplayName() 
                    + "\",\"letterCode\":\"" + currencyCode 
                    + "\",\"numberCode\":\"" + currency.getNumericCodeAsString() 
                    + "\",\"symbol\":\"" + currency.getSymbol() 
                    + "\",\"fractionDigits\":" 
                    + currency.getDefaultFractionDigits() + "}";
            String actual 
                    = CurrencyInfoJSONServer.getCurrencyInfo(currencyCode)
                            .replace("\n", "").replace("\r", "");
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testServerRespondsOnDefaultPortZeroParamConstructor() {
        try (CurrencyInfoJSONServer instance = new CurrencyInfoJSONServer()) {
            instance.activate();
            System.out.println("Expecting localhost response on port " 
                    + CurrencyInfoJSONServer.DEFAULT_HTTP_PORT + " from " 
                    + instance.toString());
            Currency currency = CurrencyChooser.chooseCurrency();
            String locator = "http://localhost:" 
                    + CurrencyInfoJSONServer.DEFAULT_HTTP_PORT + "/" 
                    + currency.getCurrencyCode();
            String key = "User-Agent";
            String value = "Java/" + System.getProperty("java.version");
            assertDoesNotThrow(() -> {
                URI uri = new URI(locator);
                URL url = uri.toURL();
                HttpURLConnection conn 
                        = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty(key, value);
                int expected = HttpURLConnection.HTTP_OK;
                int actual = conn.getResponseCode();
                assertEquals(actual, expected);
            });
        }
    }
    
    @Test
    public void testZeroParamConstructorSetsDefaultHTTPPort() {
        CurrencyInfoJSONServer instance = new CurrencyInfoJSONServer();
        int expected = CurrencyInfoJSONServer.DEFAULT_HTTP_PORT;
        int actual = instance.getPort();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testZeroParamConstructorSetsDefaultLocale() {
        CurrencyInfoJSONServer instance = new CurrencyInfoJSONServer();
        Locale expected = Locale.getDefault();
        Locale actual = instance.getLocale();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testPortNumberParamConstructorSetsSpecifiedPort() {
        int expected = CurrencyInfoJSONServer.DEFAULT_HTTP_PORT 
                + (RANDOM.nextInt(256) - 128);
        CurrencyInfoJSONServer instance = new CurrencyInfoJSONServer(expected);
        int actual = instance.getPort();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testPortNumberParamConstructorSetsDefaultLocale() {
        int port = CurrencyInfoJSONServer.DEFAULT_HTTP_PORT 
                + (RANDOM.nextInt(256) - 128);
        CurrencyInfoJSONServer instance = new CurrencyInfoJSONServer(port);
        Locale expected = Locale.getDefault();
        Locale actual = instance.getLocale();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testLocaleParamConstructorSetsDefaultPort() {
        int expected = CurrencyInfoJSONServer.DEFAULT_HTTP_PORT;
        for (Locale locale : LOCALES) {
            CurrencyInfoJSONServer instance 
                    = new CurrencyInfoJSONServer(locale);
            int actual = instance.getPort();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testLocaleParamConstructorSetsSpecifiedLocale() {
        for (Locale expected : LOCALES) {
            CurrencyInfoJSONServer instance 
                    = new CurrencyInfoJSONServer(expected);
            Locale actual = instance.getLocale();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testTwoParamConstructorSetsSpecifiedPort() {
        int expected = CurrencyInfoJSONServer.DEFAULT_HTTP_PORT 
                + (RANDOM.nextInt(256) - 128);
        Locale locale = chooseLocale();
        CurrencyInfoJSONServer instance = new CurrencyInfoJSONServer(expected, 
                locale);
        int actual = instance.getPort();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testTwoParamConstructorSetsSpecifiedLocale() {
        int port = CurrencyInfoJSONServer.DEFAULT_HTTP_PORT 
                + (RANDOM.nextInt(256) - 128);
        for (Locale expected : LOCALES) {
            CurrencyInfoJSONServer instance 
                    = new CurrencyInfoJSONServer(port, expected);
            Locale actual = instance.getLocale();
            assertEquals(actual, expected);
        }
    }
    
    @Test
    public void testPortParamConstructorRejectsNegativePort() {
        int badPort = -RANDOM.nextInt(Short.MAX_VALUE) - 1;
        String msg = "Constructor should reject port " + badPort;
        Throwable t = assertThrows(() -> {
            Object badInstance = new CurrencyInfoJSONServer(badPort);
            System.out.println(msg + ", not created instance " 
                    + badInstance.toString());
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        String numStr = Integer.toString(badPort);
        String containsMsg = "Exception message should contain \"" + numStr 
                + "\"";
        assert excMsg.contains(numStr) : containsMsg;
    }
    
    @Test
    public void testTwoParamConstructorRejectsNegativePort() {
        int badPort = -RANDOM.nextInt(Short.MAX_VALUE) - 1;
        Locale locale = LOCALES[RANDOM.nextInt(NUMBER_OF_LOCALES)];
        String msg = "Constructor should reject port " + badPort;
        Throwable t = assertThrows(() -> {
            Object badInstance = new CurrencyInfoJSONServer(badPort, locale);
            System.out.println(msg + ", not created instance " 
                    + badInstance.toString());
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        String numStr = Integer.toString(badPort);
        String containsMsg = "Exception message should contain \"" + numStr 
                + "\"";
        assert excMsg.contains(numStr) : containsMsg;
    }
    
    @Test
    public void testPortParamConstructorRejectsOutOfRangePort() {
        int badPort = 4 * Short.MAX_VALUE + RANDOM.nextInt(Byte.MAX_VALUE) + 1;
        String msg = "Constructor should reject port " + badPort;
        Throwable t = assertThrows(() -> {
            Object badInstance = new CurrencyInfoJSONServer(badPort);
            System.out.println(msg + ", not created instance " 
                    + badInstance.toString());
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        String numStr = Integer.toString(badPort);
        String containsMsg = "Exception message should contain \"" + numStr 
                + "\"";
        assert excMsg.contains(numStr) : containsMsg;
    }
    
    @Test
    public void testTwoParamConstructorRejectsOutOfRangePort() {
        int badPort = 4 * Short.MAX_VALUE + RANDOM.nextInt(Byte.MAX_VALUE) + 1;
        Locale locale = LOCALES[RANDOM.nextInt(NUMBER_OF_LOCALES)];
        String msg = "Constructor should reject port " + badPort;
        Throwable t = assertThrows(() -> {
            Object badInstance = new CurrencyInfoJSONServer(badPort, locale);
            System.out.println(msg + ", not created instance " 
                    + badInstance.toString());
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        String numStr = Integer.toString(badPort);
        String containsMsg = "Exception message should contain \"" + numStr 
                + "\"";
        assert excMsg.contains(numStr) : containsMsg;
    }
    
}
