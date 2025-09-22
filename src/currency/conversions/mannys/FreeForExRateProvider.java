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
package currency.conversions.mannys;

import currency.conversions.ExchangeRateProvider;

import java.io.InputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Currency;
import java.util.Scanner;

/**
 * Provides access to Manny's Free Currency Converter API. To use this, you need 
 * an API key, and to place that API key in an environment variable called 
 * "FOREX_API_KEY". Go to the website <a 
 * href="https://free.currencyconverterapi.com">currencyconverterapi.com</a> for 
 * more information.
 * @deprecated Since late 2024, Manny's Free Currency Converter API is down 
 * indefinitely. It might never come back online. But as I don't know that for a 
 * fact, I am not marking this class for removal. Theoretically, it's possible 
 * that one day I might be able to un-deprecate it.
 * @author Alonso del Arte
 */
@Deprecated
public class FreeForExRateProvider 
        implements ExchangeRateProvider {
    
    private static final String QUERY_PATH_BEGIN 
            = "https://free.currconv.com/api/v7/convert?q=";
    
    private static final String QUERY_PATH_CONNECTOR = "&compact=ultra&apiKey=";
    
    private static final String API_KEY = System.getenv("FOREX_API_KEY");
    
    private static final String USER_AGENT_ID = "Java/"
            + System.getProperty("java.version");
    
    static double makeAPICall(Currency source, Currency target) {
        String queryPath = QUERY_PATH_BEGIN + source.getCurrencyCode() + '_' 
                + target.getCurrencyCode() + QUERY_PATH_CONNECTOR + API_KEY;
        try {
            URI uri = new URI(queryPath);
            URL queryURL = uri.toURL();
            HttpURLConnection connection 
                    = (HttpURLConnection) queryURL.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT_ID);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream stream = (InputStream) connection.getContent();
                Scanner scanner = new Scanner(stream);
                String quote = scanner.nextLine();
                return Double.parseDouble(quote
                        .substring(quote.indexOf(':') + 1, quote.indexOf('}')));
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
    
    /**
     * Gives the rate for a currency conversion. This function calls Manny's 
     * Free Currency Converter API.
     * @param source The currency to convert from. For example, United States 
     * dollars (USD).
     * @param target The currency to convert to. For example, euros (EUR).
     * @return The rate. For example, 0.89854 as of September 16, 2024.
     * @throws RuntimeException If the API returns an HTTP status code other 
     * than HTTP OK (200), if there is an unexpected I/O problem, or if the URI 
     * for the API has a syntax error. In the latter two cases, the exception 
     * object will wrap a specific checked exception.
     * @throws NumberFormatException If {@code target} is not a currency 
     * recognized by the currency conversion API, such as a historical currency.
     */
    @Override
    public double getRate(Currency source, Currency target) {
        // TODO: Implement rate quote caching
        return makeAPICall(source, target);
    }
    
}
