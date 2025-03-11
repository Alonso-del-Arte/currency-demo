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
 * key.
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

    // TODO: Write tests for this
    @Override
    public Set<Currency> supportedCurrencies() {
        return SUPPORTED_CURRENCIES;
    }
    
    // TODO: Write tests for this
    @Override
    public double getRate(Currency source, Currency target) {
        return -1.0;
    }
    
}
