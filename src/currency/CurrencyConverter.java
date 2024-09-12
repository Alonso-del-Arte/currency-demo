/*
 * Copyright (C) 2024 Alonso del Arte
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
    
    private final ExchangeRateProvider exchangeRateProvider;
    
    // TODO: Deprecate once instance equivalent is available    
    /**
     * Gives the rate for a currency conversion. This function calls Manny's 
     * Free Currency Converter API.
     * @param source The currency to convertOld from. For example, United States 
 dollars (USD).
     * @param target The currency to convertOld to. For example, euros (EUR).
     * @return The rate as a <code>String</code> (this is to avoid loss of 
     * precision when passing the value to the <code>BigDecimal</code> 
     * constructor).
     * @throws RuntimeException If the API returns an HTTP status code other 
     * than HTTP OK (200), if there is an unexpected I/O problem, or if the URI 
     * for the API has a syntax error. In the latter two cases, the exception 
     * object will wrap a specific checked exception.
     * @throws NumberFormatException If <code>target</code> is not a currency 
     * recognized by the currency conversion API, such as a historical currency.
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
    
    // TODO: Deprecate once instance equivalent is available    
    /**
     * Converts a money amount to a specified currency. This static function 
     * will soon be deprecated in favor of instance functions that can use 
     * different currency conversion APIs.
     * @param source The original money amount. For example, $100.00 in United 
     * States dollars (USD).
     * @param target The currency to convertOld to. For example, euros (EUR).
     * @return The converted amount. In the example, which I ran on July 30, 
     * 2024, the result was &euro;92.47.
     * @throws RuntimeException If the API returns an HTTP status code other 
     * than HTTP OK (200), if there is an unexpected I/O problem, or if the URI 
     * for the API has a syntax error. In the latter two cases, the exception 
     * object will wrap a specific checked exception.
     * @throws NumberFormatException If <code>target</code> is not a currency 
     * recognized by the currency conversion API, such as a historical currency.
     */
    public static MoneyAmount convertOld(MoneyAmount source, Currency target) {
        double original = source.getFullAmountInCents();
        Currency sourceCurrency = source.getCurrency();
        int sourceDecPlaceCounter = sourceCurrency.getDefaultFractionDigits();
        while (sourceDecPlaceCounter > 0) {
            original /= 10;
            sourceDecPlaceCounter--;
        }
        double multiplicand = Double.parseDouble(getRate(sourceCurrency, 
                target));
        double converted = original * multiplicand;
        int targetDecPlaceCounter = target.getDefaultFractionDigits();
        double floored = Math.floor(converted);
        double divs = converted - floored;
        while (targetDecPlaceCounter > 0) {
            divs *= 10;
            targetDecPlaceCounter--;
        }
        long units = (long) floored;
        short divisions = (short) divs;
        return new MoneyAmount(units, target, divisions);
    }
    
    /**
     * Discloses the rate provider this converter is using.
     * @return The rate provider given to the constructor.
     */
    public ExchangeRateProvider getProvider() {
        return this.exchangeRateProvider;
    }
    
    // TODO: Write tests for this
    public MoneyAmount convert(MoneyAmount source, Currency target) {
        return source.negate();
    }
    
    /**
     * Constructor.
     * @param rateProvider The rate provider to use. For example, an instance of 
     * {@link MannysCurrencyConverterAPIAccess}.
     */
    public CurrencyConverter(ExchangeRateProvider rateProvider) {
        if (rateProvider == null ) {
            String excMsg = "Rate provider should not be null";
            throw new NullPointerException(excMsg);
        }
        this.exchangeRateProvider = rateProvider;
    }
    
}
