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

import java.io.Closeable;
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

/**
 *
 * @author Alonso del Arte
 */
public class CurrencyInfoJSONServer implements Closeable {
    
    /**
     * The default HTTP port. This constant is used by the constructors that 
     * don't take a port number.
     */
    public static final int DEFAULT_HTTP_PORT = 8080;
    
    /**
     * How many seconds to wait before closing down the server socket. May close 
     * sooner if there are no pending requests.
     */
    public static final int DEFAULT_CLOSING_DELAY = 2;
    
    /**
     * The content type specification "application/json; charset=UTF-8".
     */
    static final String CONTENT_TYPE_SPECIFICATION 
            = "application/json; charset=" + StandardCharsets.UTF_8.toString();
    
    private static final int MAX_PORT_NUMBER = 4 * Short.MAX_VALUE;
    
    private final int portNum;
    
    private final Locale loc;
    
    private HttpServer httpServer;
    
    private final HttpHandler handler = (HttpExchange exchange) -> {
        final Headers headers = exchange.getResponseHeaders();
//        final String method = exchange.getRequestMethod().toUpperCase();
//        switch (method) {
//            case "GET":
                String responseBody = "{\"value\":\"NOT READY\"}";
                headers.set("Content-Type", CONTENT_TYPE_SPECIFICATION);
                byte[] rawResponseBody 
                        = responseBody.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 
                        rawResponseBody.length);
                exchange.getResponseBody().write(rawResponseBody);
//                break;
//            case "OPTIONS":
//                headers.set("Allow", "GET,OPTIONS,PUT");
//                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
//                break;
//            default:
//                headers.set("Allow", "GET,OPTIONS,PUT");
//                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 
//                        -1);
//        }
    };
    
    /**
     * Gives the port number that was assigned at the time of construction.
     * @return The port number. For example, 8080.
     */
    public int getPort() {
        return this.portNum;
    }

    /**
     * Gives the locale that was assigned at the time of construction.
     * @return The locale. For example, {@code Locale.CANADA_FRENCH}.
     */
    public Locale getLocale() {
        return this.loc;
    }

    static String getCurrencyInfo(String currencyCode) {
        try {
            Currency currency = Currency.getInstance(currencyCode);
            return "{\"name\":\"" + currency.getDisplayName() 
                    + "\",\"letterCode\":\"" + currencyCode 
                    + "\",\"numberCode\":\"" + currency.getNumericCodeAsString() 
                    + "\",\"symbol\":\"" + currency.getSymbol() 
                    + "\",\"fractionDigits\":" 
                    + currency.getDefaultFractionDigits() + "}";
        } catch (IllegalArgumentException iae) {
            return "{\"result\":\"error\",\"error-type\":\"unsupported-code\"}";
        }
    }
    
    public void activate() {
        //
    }
    
    @Override
    public void close() {
        this.httpServer.stop(DEFAULT_CLOSING_DELAY);
        System.out.println("Stopped server localhost on port " + this.portNum);
    }
    
    /**
     * Auxiliary constructor. Sets port to {@link #DEFAULT_HTTP_PORT} and locale 
     * to {@code Locale.getDefault()}.
     */
    public CurrencyInfoJSONServer() {
        this(DEFAULT_HTTP_PORT, Locale.getDefault());
    }
    
    /**
     * Auxiliary constructor. Sets the locale to the default locale.
     * @param port The port number to use. For example, 8090.
     * @throws IllegalArgumentException If {@code port} is negative or 
     * excessive.
     */
    public CurrencyInfoJSONServer(int port) {
        this(port, Locale.getDefault());
    }
    
    /**
     * Auxiliary constructor. Sets the port to {@link #DEFAULT_HTTP_PORT}.
     * @param locale The locale. For example, {@code Locale.CANADA_FRENCH}.
     */
    public CurrencyInfoJSONServer(Locale locale) {
        this(DEFAULT_HTTP_PORT, locale);
    }
    
    /**
     * Primary constructor.
     * @param port The port number to use. For example, 8090.
     * @param locale The locale. For example, {@code Locale.CANADA_FRENCH}.
     * @throws IllegalArgumentException If {@code port} is negative.
     */
    public CurrencyInfoJSONServer(int port, Locale locale) {
        if (port < 0 || port > MAX_PORT_NUMBER) {
            String excMsg = "Port number " + port + " is not valid";
            throw new IllegalArgumentException(excMsg);
        }
        this.portNum = port;
        this.loc = locale;
//        String hostname = "localhost";
//        try {
//            this.httpServer = HttpServer
//                    .create(new InetSocketAddress(hostname, this.portNum), 1);
//            this.httpServer.createContext("/", this.handler);
//            this.httpServer.start();
//            System.out.println("Started server " + hostname + " on port " 
//                    + this.portNum);
//        } catch (IOException ioe) {
//            throw new RuntimeException(ioe);
//        }
    }
    
    public static void main(String[] args) {
        //
    }
    
}
