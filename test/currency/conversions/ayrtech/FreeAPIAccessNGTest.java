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
package currency.conversions.ayrtech;

import currency.CurrencyChooser;
import currency.CurrencyPair;
import currency.SpecificCurrenciesSupport;
import currency.conversions.ConversionRateQuote;
import currency.conversions.ExchangeRateProvider;

import java.io.InputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testframe.api.Asserters.assertContainsSame;
import static org.testframe.api.Asserters.assertDoesNotThrow;
import static org.testframe.api.Asserters.assertThrows;

import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests of the FreeAPIAccess class. This API requires an API key. Go to <a 
 * href="https://www.exchangerate-api.com">https://www.exchangerate-api.com</a> 
 * to sign up for your own API key, then put it in an environment variable 
 * called FOREX_API_KEY. When I signed up, I was given a quota of 1,500 requests 
 * per month, which should be sufficient for my purposes.
 * <p>There's also an open API which doesn't use an API key but is very tightly 
 * rate-limited. I don't remember the path from the URL above to the page 
 * describing how to access the open API.</p>
 * <p>Assuming you have a valid API key, a working Internet connection and the 
 * API is working as expected, it can still happen that the API might have a few 
 * currencies which your system's Java currency file does not recognize. This 
 * test class's {@link #setUpClass()} procedure will list unrecognized 
 * currencies. For example, on my system, Faroese kr&oacute;na (FOK) are not 
 * recognized.</p>
 * @author Alonso del Arte
 */
public class FreeAPIAccessNGTest {
    
    private static final double TEST_DELTA = 0.01;
    
    private static final Currency U_S_DOLLARS = Currency.getInstance(Locale.US);
    
    private static final Currency EAST_CARIBBEAN_DOLLARS 
            = Currency.getInstance("XCD");
    
    private static final String API_KEY = System.getenv("FOREX_API_KEY");
    
    private static final String QUERY_PATH_BEGIN 
            = "https://v6.exchangerate-api.com/v6/" + API_KEY;

    private static final String USER_AGENT_ID = "Java/"
            + System.getProperty("java.version");
    
    private static final Set<Currency> ALL_CURRENCIES 
            = Currency.getAvailableCurrencies();
    
    private static final int MAX_NUMBER_OF_CURRENCIES = ALL_CURRENCIES.size();
    
    private static final Set<Currency> SUPPORTED_CURRENCIES 
            = new HashSet<>(MAX_NUMBER_OF_CURRENCIES);
    
    private static final Map<String, Currency> CURRENCY_CODES_MAP 
            = new HashMap<>(MAX_NUMBER_OF_CURRENCIES);
    
    private static final Map<CurrencyPair, ConversionRateQuote> QUOTE_MAP 
            = new HashMap<>(MAX_NUMBER_OF_CURRENCIES);
    
    private static final FreeAPIAccess TEST_INSTANCE = new FreeAPIAccess();
    
    private static String minify(String endPoint) throws IOException {
        String queryPath = QUERY_PATH_BEGIN + endPoint;
        StringBuilder builder = new StringBuilder();
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
                while (scanner.hasNext()) {
                    String line = scanner.nextLine().replace("\n", "")
                            .replace("\r", "");
                    builder.append(line);
                }
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
        return builder.toString();
    }
    
    /**
     * Populates the list of expected supported currencies. If any currencies 
     * are not recognized, they'll be printed to {@code System.err} but 
     * hopefully the tests will still run.
     * @throws IOException If there is any sort of problem connecting to the 
     * API. No tests will run if this exception arises.
     */
    @BeforeClass
    public void setUpClass() throws IOException {
        String codesEndPoint = "/codes";
        String input = minify(codesEndPoint);
        Pattern iso4217CodePattern = Pattern.compile("\"[A-Z]{3}\"");
        Matcher matcher = iso4217CodePattern.matcher(input);
        while (matcher.find()) {
            String currencyCode = matcher.group().substring(1, 4);
            try {
                Currency currency = Currency.getInstance(currencyCode);
                CURRENCY_CODES_MAP.put(currencyCode, currency);
                SUPPORTED_CURRENCIES.add(currency);
            } catch (IllegalArgumentException iae) {
                int beginIndex = input.indexOf(currencyCode) + 6;
                int endIndex = input.indexOf('\"', beginIndex + 1);
                String displayName = input.substring(beginIndex, endIndex);
                System.err.println("Did not recognize " + currencyCode 
                        + ", said to be " + displayName);
            }
        }
        String ratesEndPoint = "/latest/USD";
        String ratesResponse = minify(ratesEndPoint);
        int currIndex = ratesResponse.indexOf(" \"USD\":1,") + 7;
        boolean hasNext = true;
        while (hasNext) {
            currIndex = ratesResponse.indexOf("\"", currIndex) + 1;
            String key = ratesResponse.substring(currIndex, currIndex + 3);
            if (CURRENCY_CODES_MAP.containsKey(key)) {
                Currency currency = CURRENCY_CODES_MAP.get(key);
                CurrencyPair currencies = new CurrencyPair(U_S_DOLLARS, currency);
                currIndex = ratesResponse.indexOf(":", currIndex) + 1;
                int commaIndex = ratesResponse.indexOf(",", currIndex);
                if (commaIndex < 0) {
                    commaIndex = ratesResponse.indexOf("\u007D", currIndex);
                    hasNext = false;
                }
                String numStr = ratesResponse.substring(currIndex, commaIndex);
                double rate = Double.parseDouble(numStr);
                ConversionRateQuote value = new ConversionRateQuote(currencies, 
                        rate, LocalDateTime.now());
                System.out.println("Conversion for " + currencies.toString() 
                        + " reported as " + rate);
                QUOTE_MAP.put(currencies, value);
            } else {
                currIndex = ratesResponse.indexOf("\"", currIndex + 4);
            }
        }
    }
    
    @Test
    public void testGetBaseCurrency() {
        System.out.println("getBaseCurrency");
        Currency expected = CurrencyChooser.chooseCurrency();
        FreeAPIAccess instance = new FreeAPIAccess(expected);
        Currency actual = instance.getBaseCurrency();
        String message = "Base currency should be " + expected.getDisplayName();
        assertEquals(actual, expected, message);
    }
    
    /**
     * Test of the supportedCurrencies function, of the FreeAPIAccess class. 
     * This test does not explicitly make an API call directly, and the called 
     * function should not make an API call either. Of course I did make an API 
     * call to get the list of currencies supported by the API in the first 
     * place. I used a Web browser and then used a regular expression to filter 
     * out the numbers and just have the ISO-4217 currency codes.
     */
    @Test
    public void testSupportedCurrencies() {
        System.out.println("supportedCurrencies");
        Set<Currency> actual = TEST_INSTANCE.supportedCurrencies();
        assertContainsSame(SUPPORTED_CURRENCIES, actual);
    }

    @Test
    public void testSupportedCurrenciesDoesNotLeakField() {
        Set<Currency> initial = TEST_INSTANCE.supportedCurrencies();
        Currency currency = CurrencyChooser.chooseCurrency(
                cur -> !initial.contains(cur)
        );
        String msg = "Trying to add " + currency.getDisplayName() + " (" 
                + currency.getCurrencyCode() 
                + ") to reported set should not leak field nor cause exception";
        assertDoesNotThrow(() -> {
            Set<Currency> expected = new HashSet<>(initial);
            initial.add(currency);
            Set<Currency> actual = TEST_INSTANCE.supportedCurrencies();
            assertContainsSame(expected, actual, msg);
        }, msg);
    }
    
    @Test
    public void testGetRateSourceSameAsTarget() {
        double expected = 1.0;
        double delta = 0.00001;
        String msgPart = " should be " + expected + " with variance " + delta;
        for (Currency currency : SUPPORTED_CURRENCIES) {
            CurrencyPair currencies = new CurrencyPair(currency, currency);
            double actual = TEST_INSTANCE.getRate(currencies);
            String message = "Given " + currency.getDisplayName() 
                    + ", exchange rate for " + currencies.toString() + msgPart;
            assertEquals(actual, expected, delta, message);
        }
    }
    
    // TODO: Write testGetRateSourceSameAsTargetAlreadyUnwrapped
    
    @Test
    public void testNoAPICallGetRateWhenSourceSameAsTargetAlreadyUnwrapped() {
        AccessWithAPICallCounter instance = new AccessWithAPICallCounter();
        String msgPart = " should not have needed an API call";
        int expected = 0;
        for (Currency currency : SUPPORTED_CURRENCIES) {
            CurrencyPair currencies = new CurrencyPair(currency, currency);
            double rate = instance.getRate(currencies);
            int actual = instance.callCountSoFar;
            String message = "Getting rate " + rate + " for " 
                    + currencies.toString() + msgPart;
            assertEquals(actual, expected, message);
        }
    }
    
    @Test
    public void testGetRateUSDToXCDAlreadyUnwrapped() {
        double expected = 2.7;
        double actual = TEST_INSTANCE.getRate(U_S_DOLLARS, 
                EAST_CARIBBEAN_DOLLARS);
        String message = "Reckoning conversion of " + U_S_DOLLARS.getDisplayName() 
                + " (" + U_S_DOLLARS.getCurrencyCode() + ") to " 
                + EAST_CARIBBEAN_DOLLARS.getDisplayName() + " (" 
                + EAST_CARIBBEAN_DOLLARS.getCurrencyCode() + ")";
        assertEquals(actual, expected, TEST_DELTA, message);
    }
    
    @Test
    public void testGetRateUSDToXCD() {
        CurrencyPair currencies = new CurrencyPair(U_S_DOLLARS, 
                EAST_CARIBBEAN_DOLLARS);
        double expected = 2.7;
        double actual = TEST_INSTANCE.getRate(currencies);
        String message = "Reckoning conversion of " + U_S_DOLLARS.getDisplayName() 
                + " (" + U_S_DOLLARS.getCurrencyCode() + ") to " 
                + EAST_CARIBBEAN_DOLLARS.getDisplayName() + " (" 
                + EAST_CARIBBEAN_DOLLARS.getCurrencyCode() + ")";
        assertEquals(actual, expected, TEST_DELTA, message);
    }
    
    @Test
    public void testGetRateXCDToUSDAlreadyUnwrapped() {
        double expected = 0.37037037037037035;
        double actual = TEST_INSTANCE.getRate(EAST_CARIBBEAN_DOLLARS, 
                U_S_DOLLARS);
        String message = "Reckoning conversion of " 
                + EAST_CARIBBEAN_DOLLARS.getDisplayName() + " (" 
                + EAST_CARIBBEAN_DOLLARS.getCurrencyCode() + ") to " 
                + U_S_DOLLARS.getDisplayName() + " (" 
                + U_S_DOLLARS.getCurrencyCode() + ")";
        assertEquals(actual, expected, TEST_DELTA, message);
    }
    
    @Test
    public void testGetRateXCDToUSD() {
        CurrencyPair currencies = new CurrencyPair(EAST_CARIBBEAN_DOLLARS, 
                U_S_DOLLARS);
        double expected = 0.37037037037037035;
        double actual = TEST_INSTANCE.getRate(currencies);
        String message = "Reckoning conversion of " 
                + EAST_CARIBBEAN_DOLLARS.getDisplayName() + " (" 
                + EAST_CARIBBEAN_DOLLARS.getCurrencyCode() + ") to " 
                + U_S_DOLLARS.getDisplayName() + " (" 
                + U_S_DOLLARS.getCurrencyCode() + ")";
        assertEquals(actual, expected, TEST_DELTA, message);
    }
    
    /**
     * Test of the getRate function, of the FreeAPIAccess class.
     */
    @Test
    public void testGetRate() {
        System.out.println("getRate");
        for (ConversionRateQuote quote : QUOTE_MAP.values()) {
            CurrencyPair currencies = quote.getCurrencies();
            double expected = quote.getRate();
            double actual = TEST_INSTANCE.getRate(currencies);
            Currency from = currencies.getFromCurrency();
            Currency to = currencies.getToCurrency();
            String message = "Getting conversion rate for " 
                    + from.getDisplayName() + " (" + from.getCurrencyCode() 
                    + ") to " + to.getDisplayName() + " (" 
                    + to.getCurrencyCode() + ")";
            assertEquals(actual, expected, TEST_DELTA, message);
        }
    }
    
    @Test
    public void testAuxConstructorSetsUnitedStatesDollarsAsBaseCurrency() {
        FreeAPIAccess instance = new FreeAPIAccess();
        Currency expected = U_S_DOLLARS;
        Currency actual = instance.getBaseCurrency();
        String message = "Default currency should be " 
                + expected.getDisplayName();
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testPrimaryConstructorRejectsNullCurrency() {
        String msg = "Constructor should reject null currency";
        Throwable t = assertThrows(() -> {
            FreeAPIAccess badInstance = new FreeAPIAccess(null);
            System.out.println("Should not have been able to create " 
                    + badInstance.toString() + " with null base currency");
        }, NullPointerException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be null";
        System.out.println("\"" + excMsg + "\"");
    }
    
    private static class AccessWithAPICallCounter extends FreeAPIAccess {
        
        private int callCountSoFar = 0;
        
        @Override
        InputStream makeAPICall() {
            this.callCountSoFar++;
            return super.makeAPICall();
        }
        
    }
    
}
