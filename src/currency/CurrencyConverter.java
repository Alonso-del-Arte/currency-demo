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
     * Gives the rate for a currency conversion. This function calls Manny's 
     * Free Currency Converter API.
     * @param source The currency to convert from. For example, United States 
     * dollars (USD).
     * @param target The currency to convert to. For example, euros (EUR).
     * @return The rate as a <code>String</code> (this is to avoid loss of 
     * precision when passing the value to the <code>BigDecimal</code> 
     * constructor).
     * @throws RuntimeException If the API returns an HTTP status code other 
     * than HTTP OK (200), if there is an unexpected I/O problem, or if the URI 
     * for the API has a syntax error. In the latter two cases, the exception 
     * object will wrap a specific checked exception.
     */
    public static String getRate(Currency source, Currency target) {
        String queryPath 
                = "https://free.currconv.com/api/v7/convert?q="
                + source.getCurrencyCode() + "_" 
                + target.getCurrencyCode()
                + "&compact=ultra&apiKey=" + API_KEY;
        try {
            URI uri = new URI(queryPath);
            URL queryURL = uri.toURL();
            HttpURLConnection connection 
                    = (HttpURLConnection) queryURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", USER_AGENT_ID);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream stream = (InputStream) connection.getContent();
                Scanner scanner = new Scanner(stream);
                String quote = scanner.nextLine();
                return quote.substring(quote.indexOf(':') + 1, 
                        quote.indexOf('}'));
            } else {
                String excMsg = "Query " + queryPath + " returned status " 
                        + responseCode;
                throw new RuntimeException(excMsg);
            }
        } catch (IOException ioe) {
            String excMsg = "Unexpected I/O problem encountered";
            throw new RuntimeException(excMsg, ioe);
        } catch (URISyntaxException urise) {
            String excMsg = "Query path <" + queryPath + "> is not valid";
            throw new RuntimeException(excMsg, urise);
        }
    }
    
    // TODO: Write tests for this
    public static MoneyAmount convert(MoneyAmount source, Currency target) {
        return source;
    }
    
}
