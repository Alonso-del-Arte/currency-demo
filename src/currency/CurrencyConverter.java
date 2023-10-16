/*
 * Copyright (C) 2023 Alonso del Arte
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package currency;

import java.io.InputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Currency;
import java.util.Scanner;

/**
 *
 * @author Alonso del Arte
 */
public class CurrencyConverter {
    
    private static final String API_KEY = System.getenv("FOREX_API_KEY");
    
    private static final String USER_AGENT_ID = "Java/"
            + System.getProperty("java.version");
    
    /**
     * Gives the rate for a currency conversion.
     * @param source The currency to convert from. For example, United States 
     * dollars (USD).
     * @param target The currency to convert to. For example, euros (EUR).
     * @return The rate as a <code>String</code> (this is to avoid loss of 
     * precision when passing the value to the <code>BigDecimal</code> 
     * constructor.
     */
    public static String getRate(Currency source, Currency target) {
        return "-2.0";
    }
    
}
