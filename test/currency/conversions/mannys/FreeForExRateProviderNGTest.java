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

import currency.CurrencyChooser;
import currency.conversions.ExchangeRateProvider;

import java.io.InputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.Scanner;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the FreeForExRateProvider class.
 * @author Alonso del Arte
 */
public class FreeForExRateProviderNGTest {
    
    private static final double TEST_DELTA = 0.01;
    
    private static final Currency U_S_DOLLARS = Currency.getInstance(Locale.US);
    
    private static final Currency EAST_CARIBBEAN_DOLLARS 
            = Currency.getInstance("XCD");
    
    @Test
    public void testGetRateNoConversionNeeded() {
        ExchangeRateProvider instance 
                = new FreeForExRateProvider();
        Currency currency = CurrencyChooser.chooseCurrency();
        double expected = 1.0;
        double actual = instance.getRate(currency, currency);
        String iso4217Code = currency.getCurrencyCode();
        String message = "No conversion needed for " + iso4217Code + " to " 
                + iso4217Code;
        assertEquals(actual, expected, TEST_DELTA, message);
    }
    
    @Test
    public void testGetRateForUSDollarsToEastCaribbeanDollars() {
        ExchangeRateProvider instance 
                = new FreeForExRateProvider();
        double expected = 2.702;
        double actual = instance.getRate(U_S_DOLLARS, EAST_CARIBBEAN_DOLLARS);
        assertEquals(actual, expected, TEST_DELTA);
    }
    
    @Test
    public void testGetRateForEastCaribbeanDollarsToUSDollars() {
        ExchangeRateProvider instance 
                = new FreeForExRateProvider();
        double expected = 0.37;
        double actual = instance.getRate(EAST_CARIBBEAN_DOLLARS, U_S_DOLLARS);
        assertEquals(actual, expected, TEST_DELTA);
    }
    
    /**
     * Test of the getRate function, of the FreeForExRateProvider 
 class.
     */
    @Test
    public void testGetRate() {
        System.out.println("getRate");
        ExchangeRateProvider instance 
                = new FreeForExRateProvider();
        Currency firstTarget = CurrencyChooser
                .chooseCurrencyOtherThan(U_S_DOLLARS);
        String dollarsDisplayName = U_S_DOLLARS.getDisplayName();
        String dollarsISO4217Code = U_S_DOLLARS.getCurrencyCode();
        String firstTargetDisplayName = firstTarget.getDisplayName();
        String firstTargetISO4217Code = firstTarget.getCurrencyCode();
        System.out.println("Inquiring rate of conversion from " 
                + dollarsDisplayName + " (" + dollarsISO4217Code + ") to " 
                + firstTargetDisplayName + " (" + firstTargetISO4217Code + ")");
        double fromDollars = instance.getRate(U_S_DOLLARS, firstTarget);
        double toDollars = instance.getRate(firstTarget, U_S_DOLLARS);
        double expected = 1.0;
        double actual = fromDollars * toDollars;
        String message = "Rate of conversion from " + dollarsDisplayName + " (" 
                + dollarsISO4217Code + ") to " + firstTargetDisplayName + " (" 
                + firstTargetISO4217Code + ") is said to be " + fromDollars 
                + ", and vice-versa is said to be " + toDollars;
        System.out.println(message);
        assertEquals(actual, expected, TEST_DELTA, message);
    }
    
    private static double getRateFromMannysAPI(Currency source, Currency target, 
            String message) {
        final String apiKey = System.getenv("FOREX_API_KEY");
        String queryPath 
                = "https://free.currconv.com/api/v7/convert?q="
                + source.getCurrencyCode() + "_" 
                + target.getCurrencyCode()
                + "&compact=ultra&apiKey=" + apiKey;
        final String userAgentID = "Java/" 
                + System.getProperty("java.version");
        try {
            URI uri = new URI(queryPath);
            URL queryURL = uri.toURL();
            HttpURLConnection connection 
                    = (HttpURLConnection) queryURL.openConnection();
            connection.setRequestProperty("User-Agent", userAgentID);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream stream = (InputStream) connection.getContent();
                Scanner scanner = new Scanner(stream);
                String quote = scanner.nextLine();
                return Double.parseDouble(quote.substring(quote.indexOf(':') 
                        + 1, quote.indexOf('}')));
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
        } catch (NumberFormatException nfe) {
            throw new RuntimeException(message, nfe);
        }
    }
    
    @Test
    public void testGetRateActuallyCallsMannysAPI() {
        ExchangeRateProvider instance 
                = new FreeForExRateProvider();
        Currency source = CurrencyChooser.chooseCurrency();
        Currency target = CurrencyChooser.chooseCurrencyOtherThan(source);
        String message = "Inquiring Manny's API for rate of conversion from " 
                + source.getDisplayName() + " (" + source.getCurrencyCode() 
                + ") to " + target.getDisplayName() + " (" 
                + target.getCurrencyCode() + ")";
        double expected = getRateFromMannysAPI(source, target, message);
        double actual = instance.getRate(source, target);
        assertEquals(actual, expected, TEST_DELTA, message);
    }

}
