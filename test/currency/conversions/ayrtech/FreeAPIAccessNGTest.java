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
import currency.conversions.ExchangeRateProvider;

import java.io.InputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Currency;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Scanner;

import static org.testframe.api.Asserters.assertContainsSame;
import static org.testframe.api.Asserters.assertInRange;
import static org.testframe.api.Asserters.assertDoesNotThrow;

import static org.testng.Assert.*;
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
 * @author Alonso del Arte
 */
public class FreeAPIAccessNGTest {
    
    private static final String[] CURRENCY_CODES = {"AED", "AFN", "ALL", "AMD", 
        "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD", "BDT", "BGN", 
        "BHD", "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BYN", 
        "BZD", "CAD", "CDF", "CHF", "CLP", "CNY", "COP", "CRC", "CUP", "CVE", 
        "CZK", "DJF", "DKK", "DOP", "DZD", "EGP", "ERN", "ETB", "EUR", "FJD", 
        "FKP", "GBP", "GEL", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", 
        "HNL", "HRK", "HTG", "HUF", "IDR", "ILS", "INR", "IQD", "IRR", "ISK", 
        "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KMF", "KRW", "KWD", "KYD", 
        "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LYD", "MAD", "MDL", "MGA", 
        "MKD", "MMK", "MNT", "MOP", "MRU", "MUR", "MVR", "MWK", "MXN", "MYR", 
        "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", 
        "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", 
        "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLE", "SOS", "SRD", 
        "SSP", "STN", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", 
        "TTD", "TWD", "TZS", "UAH", "UGX", "USD", "UYU", "UZS", "VES", "VND", 
        "VUV", "WST", "XAF", "XCD", "XOF", "XPF", "YER", "ZAR", "ZMW"};
    
    private static final Set<Currency> SUPPORTED_CURRENCIES 
            = Set.of(CURRENCY_CODES).stream().map(
                    currencyCode -> Currency.getInstance(currencyCode)
            ).collect(Collectors.toSet());

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
        SpecificCurrenciesSupport instance = new FreeAPIAccess();
        Set<Currency> actual = instance.supportedCurrencies();
        assertContainsSame(SUPPORTED_CURRENCIES, actual);
    }

    @Test
    public void testSupportedCurrenciesDoesNotLeakField() {
        SpecificCurrenciesSupport instance = new FreeAPIAccess();
        Set<Currency> initial = instance.supportedCurrencies();
        Currency currency = CurrencyChooser.chooseCurrency(
                cur -> !initial.contains(cur)
        );
        String msg = "Trying to add " + currency.getDisplayName() + " (" 
                + currency.getCurrencyCode() 
                + ") to reported set should not leak field nor cause exception";
        assertDoesNotThrow(() -> {
            Set<Currency> expected = new HashSet<>(initial);
            initial.add(currency);
            Set<Currency> actual = instance.supportedCurrencies();
            assertContainsSame(expected, actual, msg);
        }, msg);
    }
    
    @Test
    public void testGetRateSourceSameAsTarget() {
        ExchangeRateProvider instance = new FreeAPIAccess();
        double expected = 1.0;
        double delta = 0.00001;
        String msgPart = " should be " + expected + " with variance " + delta;
        for (Currency currency : SUPPORTED_CURRENCIES) {
            CurrencyPair currencies = new CurrencyPair(currency, currency);
            double actual = instance.getRate(currencies);
            String message = "Given " + currency.getDisplayName() 
                    + ", exchange rate for " + currencies.toString() + msgPart;
            assertEquals(actual, expected, delta, message);
        }
    }
    
    @Test
    public void testNoAPICallGetRateWhenSourceSameAsTarget() {
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
    
    /**
     * Test of getRate method, of class FreeAPIAccess.
     */
    @Test
    public void testGetRate() {
        System.out.println("getRate");
        Currency source = null;
        Currency target = null;
        FreeAPIAccess instance = new FreeAPIAccess();
        double expResult = 0.0;
        double result = instance.getRate(source, target);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
        assertEquals(result, expResult, 0.0);
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
