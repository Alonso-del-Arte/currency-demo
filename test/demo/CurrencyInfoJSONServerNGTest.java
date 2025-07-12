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
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import java.util.Random;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

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
    
    @Test
    public void testDefaultHTTPPortConstant() {
        int expected = 8080;
        int actual = CurrencyInfoJSONServer.DEFAULT_HTTP_PORT;
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
    
    /**
     * Test of the getCurrencyInfo function, of the CurrencyInfoJSONServer 
     * class.
     */@org.testng.annotations.Ignore
    @Test
    public void testGetCurrencyInfo() {
        System.out.println("getCurrencyInfo");
        String currencyCode = "XTS";
        String expected = "{\"name\": \"Currency for testing\"}";
        String actual = CurrencyInfoJSONServer.getCurrencyInfo(currencyCode);
        assertEquals(actual, expected);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
    public void testConstructorRejectsNegativePort() {
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
    public void testConstructorRejectsOutOfRangePort() {
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
    
}
