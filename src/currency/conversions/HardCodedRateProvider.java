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
package currency.conversions;

import currency.CurrencyPair;
import currency.SpecificCurrenciesSupport;

import java.time.LocalDate;
import java.time.Month;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides hard-coded currency exchange rates.
 * @author Alonso del Arte
 */
public class HardCodedRateProvider implements ExchangeRateProvider, 
        SpecificCurrenciesSupport {
    
    /**
     * Gives the date that the values given by this provider were hard-coded on. 
     * It is expected that some of the values will soon go stale, while others 
     * might remain current until the next hard-coding.
     */
    public static final LocalDate DATE_OF_HARD_CODING 
            = LocalDate.of(2025, Month.MARCH, 3);
    
    private static final Currency UNITED_STATES_DOLLARS 
            = Currency.getInstance(Locale.US);
    
    private static final String[] CURRENCY_CODES = {"AUD", "BRL", "CAD", "CNY", 
        "EUR", "GBP", "HKD", "ILS", "INR", "JPY", "KRW", "MXN", "NZD", "PHP", 
        "TWD", "USD", "VND", "XAF", "XCD", "XOF", "XPF"};
    
    private static final Set<Currency> SUPPORTED_CURRENCIES 
            = Set.of(CURRENCY_CODES).stream().map(
                    currencyCode -> Currency.getInstance(currencyCode)
            ).collect(Collectors.toSet());
    
    private static final double[] HARD_CODED_RATES = {1.6116, 5.9909941, 1.45, 
        7.29, 0.95, 0.78822, 7.78, 3.6, 87.38, 149.22, 1459.18, 20.78, 1.78, 
        57.79, 32.92, 1.0, 25589.98, 625.4, 2.7, 625.39, 113.71};
    
    private static final Map<CurrencyPair, Double> QUOTES_MAP = new HashMap<>();
    
    static {
        for (int i = 0; i < CURRENCY_CODES.length; i++) {
            Currency to = Currency.getInstance(CURRENCY_CODES[i]);
            CurrencyPair key = new CurrencyPair(UNITED_STATES_DOLLARS, to);
            QUOTES_MAP.put(key, HARD_CODED_RATES[i]);
        }
    }
    
    @Override
    public Set<Currency> supportedCurrencies() {
        return new HashSet<>(SUPPORTED_CURRENCIES);
    }
    
    @Override
    public double getRate(Currency source, Currency target) {
        CurrencyPair currencies = new CurrencyPair(source, target);
        return this.getRate(currencies);
    }
    
    @Override
    public double getRate(CurrencyPair currencies) {
        if (QUOTES_MAP.containsKey(currencies)) {
            return QUOTES_MAP.get(currencies);
        } else {
            return 1.0 / QUOTES_MAP.getOrDefault(currencies.flip(), 1.0);
        }
    }
    
}
