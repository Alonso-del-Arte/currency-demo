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
import currency.conversions.ExchangeRateProvider;

import java.time.LocalDate;
import java.time.Month;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * WORK IN PROGRESS... The free API is free to use but it does require an API 
 * key. Go to <a 
 * href="https://www.exchangerate-api.com">https://www.exchangerate-api.com</a> 
 * to sign up for your own API key, then put it in an environment variable 
 * called FOREX_API_KEY. When I signed up, I was given a quota of 1,500 requests 
 * per month, which should be sufficient for my purposes.
 * @author Alonso del Arte
 */
public class FreeAPIAccess implements ExchangeRateProvider, 
        SpecificCurrenciesSupport {
    
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
    
    // TODO: Write tests for this
    @Override
    public double getRate(Currency source, Currency target) {
        return -1.0;
    }
    
}
