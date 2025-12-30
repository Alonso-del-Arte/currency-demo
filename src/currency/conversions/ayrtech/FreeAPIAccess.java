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

import currency.CurrencyPair;
import currency.SpecificCurrenciesSupport;
import currency.conversions.ConversionRateQuote;
import currency.conversions.ExchangeRateProvider;
import currency.conversions.InvertibleRateQuoteCache;
import currency.conversions.RateQuoteCache;

import java.io.InputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WORK IN PROGRESS... The free API is free to use but it does require an API 
 * key. Go to <a 
 * href="https://www.exchangerate-api.com">https://www.exchangerate-api.com</a> 
 * to sign up for your own API key, then put it in an environment variable 
 * called FOREX_API_KEY. When I signed up, I was given a quota of 1,500 requests 
 * per month, which should be sufficient for my purposes.
 * <p>However, in November, I got an e-mail saying my API access would be 
 * deactivated on November 14, 2025 unless I signed up for a paid plan. Well, I 
 * haven't signed up for a paid plan and my access hasn't been deactivated as of 
 * November 17. So I guess I'll keep working on this, and if I can get it close 
 * to finished, great, and if not, it's just abandoned.</p>
 * @author Alonso del Arte
 */
public class FreeAPIAccess implements ExchangeRateProvider, 
        SpecificCurrenciesSupport {
    
    private static final String API_KEY = System.getenv("FOREX_API_KEY");
    
    private static final String QUERY_PATH_BEGIN 
            = "https://v6.exchangerate-api.com/v6/" + API_KEY;

    private static final String USER_AGENT_ID = "Java/"
            + System.getProperty("java.version");
    
    private static final Set<Currency> SUPPORTED_CURRENCIES = new HashSet<>();
    
    private static final Set<String> CURRENCY_CODES = new HashSet<>();
    
    private static final Currency U_S_DOLLARS = Currency.getInstance(Locale.US);
    
    private final Currency baseCurrency;

    // TODO: Refactor this function to a public function in a different class
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
    
    static {
        String endPoint = "/codes";
        try {
            String input = minify(endPoint);
            Pattern iso4217CodePattern = Pattern.compile("\"[A-Z]{3}\"");
            Matcher matcher = iso4217CodePattern.matcher(input);
            while (matcher.find()) {
                String currencyCode = matcher.group().substring(1, 4);
                try {
                    Currency currency = Currency.getInstance(currencyCode);
                    CURRENCY_CODES.add(currencyCode);
                    SUPPORTED_CURRENCIES.add(currency);
                } catch (IllegalArgumentException iae) {
                    int beginIndex = input.indexOf(currencyCode) + 6;
                    int endIndex = input.indexOf('\"', beginIndex + 1);
                    String displayName = input.substring(beginIndex, endIndex);
                    System.out.println("Did not recognize " + currencyCode
                            + ", said to be " + displayName);
                }
            }
        } catch (IOException ioe) {
            System.err.println("Encountered problem accessing " + endPoint);
            System.err.println("\"" + ioe.getMessage() + "\"");
        }
    }
    
    private final RateQuoteCache quoteCache 
            = new InvertibleRateQuoteCache(cacheops.LRUCache.MAXIMUM_CAPACITY) {
        
        @Override
        public boolean needsRefresh(CurrencyPair currencies) {
            return false;
        }
        
        @Override
        public ConversionRateQuote create(CurrencyPair currencies) {
            String currencyCode = currencies.getToCurrency().getCurrencyCode();
            String endPoint = "/latest/" + currencyCode;
            try {
                String input = minify(endPoint);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
            return new ConversionRateQuote(currencies, 1.0);
        }
               
    };
    
    private static final 
            Map<CurrencyPair, ConversionRateQuote> DOLLAR_CONVERSIONS_MAP 
            = new HashMap<>();
    
    static {
        String endPoint = "/latest/USD";
        try {
            String ratesResponse = minify(endPoint);
            int currIndex = ratesResponse.indexOf(" \"USD\":1,") + 7;
            boolean hasNext = true;
            while (hasNext) {
                currIndex = ratesResponse.indexOf("\"", currIndex) + 1;
                String currencyCode = ratesResponse.substring(currIndex, 
                        currIndex + 3);
                if (CURRENCY_CODES.contains(currencyCode)) {
                    Currency currency = Currency.getInstance(currencyCode);
                    CurrencyPair currencies = new CurrencyPair(U_S_DOLLARS, 
                            currency);
                    currIndex = ratesResponse.indexOf(':', currIndex) + 1;
                    int endIndex = ratesResponse.indexOf(',', currIndex);
                    if (endIndex < 0) {
                        endIndex = ratesResponse.indexOf('\u007D', currIndex);
                        hasNext = false;
                    }
                    String numStr = ratesResponse.substring(currIndex, 
                            endIndex);
                    double rate = Double.parseDouble(numStr);
                    ConversionRateQuote value 
                            = new ConversionRateQuote(currencies, rate, 
                                    LocalDateTime.now());
                    DOLLAR_CONVERSIONS_MAP.put(currencies, value);
                } else {
                    currIndex = ratesResponse.indexOf("\"", currIndex + 4);
                }
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Retrieves the base currency.
     * @return The base currency. This is the same one that this instance was 
     * constructed with.
     */
    public Currency getBaseCurrency() {
        return this.baseCurrency;
    }
    
    /**
     * The currencies that are supported by ExchangeRate-API, minus currencies 
     * not recognized by the Java Runtime Environment's currency information 
     * file. Two historical currencies and a pseudocurrency recognized by both 
     * ExchangeRate-API and the Java Runtime Environment's currency information 
     * file have also been left out, as explained below. It is theoretically 
     * possible for anyone to add currencies to Java's currency information 
     * file, but in practice this is much too difficult to undertake in a 
     * demonstration such as this project.
     * @return The set of currencies, which includes a lot of the world's 
     * currencies but excludes the following:
     * <ul>
     * <li>The Faroese kr&oacute;na (FOK), as it's recognized by the API but not
     * by the Java currency information file.</li>
     * <li>The Guernsey pound (GGP), for the same reason.</li>
     * <li>The Manx pound (IMP), for the same reason.</li>
     * <li>The Jersey pound (JEP), for the same reason.</li>
     * <li>The Kiribati dollar (KID), for the same reason.</li>
     * <li>The North Korean won (KPW) is emphatically <em>not</em> supported by 
     * the API.</li>
     * <li>The Sierra Leonean leone (1964 &mdash; 2022, SLL), as it's labeled a 
     * historical currency by the Java currency information file.</li>
     * <li>The Tuvaluan dollar (TVD), as it's recognized by the API but not by 
     * the Java currency information file.</li>
     * <li>Special drawing rights (XDR), as it's a pseudocurrency by the 
     * parameters of this project.</li>
     * <li>The Zimbabwean dollar (ZWL), not sure what's going on with that 
     * one.</li>
     * </ul>
     */
    @Override
    public Set<Currency> supportedCurrencies() {
        return new HashSet<>(SUPPORTED_CURRENCIES);
    }
    
    String makeAPICall() {
        return "PLACEHOLDER";
    }
    
    private void processAPICall() {
        //
    }
    
    // TODO: Write tests for this
    @Override
    public double getRate(Currency source, Currency target) {
        if (source.getCurrencyCode().equals("USD")) {
            if (target.getCurrencyCode().equals("XCD")) {
                return 2.7;
            } else {
                CurrencyPair currencies = new CurrencyPair(source, target);
                if (DOLLAR_CONVERSIONS_MAP.containsKey(currencies)) {
                    ConversionRateQuote quote 
                            = DOLLAR_CONVERSIONS_MAP.get(currencies);
                    return quote.getRate();
                }
            }
        }
        if (source.getCurrencyCode().equals("XCD") 
                && target.getCurrencyCode().equals("USD")) {
            return 0.37037037037037035;
        }
        return 1.0;
    }
    
    // TODO: Write tests for this
    @Override
    public double getRate(CurrencyPair currencies) {
        if (currencies.getFromCurrency().equals(currencies.getToCurrency())) {
            return 1.0;
        }
        if (currencies.getFromCurrency().getCurrencyCode()
                .equals("USD")) {
            if (currencies.getToCurrency().getCurrencyCode().equals("USD")) {
                return 1.0;
            }
            if (currencies.getToCurrency().getCurrencyCode().equals("XCD")) {
                return 2.7;
            } else {
                if (DOLLAR_CONVERSIONS_MAP.containsKey(currencies)) {
                    ConversionRateQuote quote 
                            = DOLLAR_CONVERSIONS_MAP.get(currencies);
                    return quote.getRate();
                }
            }
        } else {
            this.makeAPICall(); // Preparing to fail the next test without 
                                // making 200 API calls
            System.out.println("Making API call for " + currencies.toString());
            CurrencyPair key = currencies.flip();
            if (DOLLAR_CONVERSIONS_MAP.containsKey(key)) {
                ConversionRateQuote quote = DOLLAR_CONVERSIONS_MAP.get(key);
                return quote.invert().getRate();
            }
        }
        if (currencies.getFromCurrency().getCurrencyCode().equals("XCD") 
                && currencies.getToCurrency().getCurrencyCode().equals("USD")) {
            return 0.37037037037037035;
        }
        return 1.0;
    }
    
    /**
     * Auxiliary constructor. The base currency is the United States dollar 
     * (USD).
     */
    public FreeAPIAccess() {
        this(U_S_DOLLARS);
    }
    
    /**
     * Primary constructor. A base currency must be specified.
     * @param base The base currency. For example, the British pound (GBP).
     * @throws NullPointerException If {@code base} is null.
     */
    public FreeAPIAccess(Currency base) {
        if (base == null) {
            String excMsg = "Base currency must not be null";
            throw new NullPointerException(excMsg);
        }
        this.baseCurrency = base;
    }
    
}
