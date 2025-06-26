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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Currency;

/**
 *
 * @author Alonso del Arte
 */
public class CurrencyInfoJSONServer {
    
    public static String getCurrencyInfo(String currencyCode) {
        return "SORRY, NOT IMPLEMENTED YET";
    }
    
    public CurrencyInfoJSONServer(int port) {
        if (port < 0 || port > 4 * Short.MAX_VALUE) {
            String excMsg = "Port " + port + " is not valid";
            throw new IllegalArgumentException(excMsg);
        }
    }
    
    public static void main(String[] args) {
        //
    }
    
}
