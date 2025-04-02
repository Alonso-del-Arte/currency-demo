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
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides hard-coded currency exchange rates. See {@link #DATE_OF_HARD_CODING} 
 * for the date that these values were hard-coded.
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
            double value = HARD_CODED_RATES[i];
            QUOTES_MAP.put(key, value);
            QUOTES_MAP.put(key.flip(), 1.0 / value);
        }
    }
    
    @Override
    public Set<Currency> supportedCurrencies() {
        return new HashSet<>(SUPPORTED_CURRENCIES);
    }
    
    /**
     * Gives the rate to convert one unit of the source currency to the target 
     * currency.
     * @param source The source to convert from. For example, United States 
     * dollars (USD).
     * @param target The target to convert one unit of {@code source} to. For 
     * example, euros (EUR).
     * @return The conversion rate. In the example as of March 3, 2025, this was 
     * 0.95.
     */
    @Override
    public double getRate(Currency source, Currency target) {
        if (!SUPPORTED_CURRENCIES.contains(source)) {
            String excMsg = "Source currency " + source.getDisplayName() + " (" 
                    + source.getCurrencyCode() + ") is not supported";
            throw new NoSuchElementException(excMsg);
        }
        if (!SUPPORTED_CURRENCIES.contains(target)) {
            String excMsg = "Target currency " + target.getDisplayName() + " (" 
                    + target.getCurrencyCode() + ") is not supported";
            throw new NoSuchElementException(excMsg);
        }
        CurrencyPair currencies = new CurrencyPair(source, target);
        return this.getRate(currencies);
    }
    
    /**
     * Gives the rate to convert one unit of the source currency to the target 
     * currency. The provided default implementation simply unwraps the {@link 
     * CurrencyPair} instance and calls the 2-parameter version of the function, 
     * which this interface does not implement.
     * @param currencies The pair of currencies, source and target. For example, 
     * United States dollars (USD) and euros (EUR).
     * @return The conversion rate. In the example as of March 3, 2025, this was 
     * 0.95.
     */
    @Override
    public double getRate(CurrencyPair currencies) {
        Currency source = currencies.getFromCurrency();
        if (!SUPPORTED_CURRENCIES.contains(source)) {
            String excMsg = "Source currency " + source.getDisplayName() + " (" 
                    + source.getCurrencyCode() + ") is not supported";
            throw new NoSuchElementException(excMsg);
        }
        if (QUOTES_MAP.containsKey(currencies)) {
            return QUOTES_MAP.get(currencies);
        } else {
            CurrencyPair key = currencies.flip();
            if (QUOTES_MAP.containsKey(key)) {
                return QUOTES_MAP.get(key);
            } else {
                CurrencyPair sourcePair 
                        = new CurrencyPair(UNITED_STATES_DOLLARS, 
                                currencies.getFromCurrency());
                CurrencyPair targetPair 
                        = new CurrencyPair(UNITED_STATES_DOLLARS, 
                                currencies.getToCurrency());
                double usdToSource = QUOTES_MAP.getOrDefault(sourcePair, 1.0);
                double usdToTarget = QUOTES_MAP.getOrDefault(targetPair, 1.0);
                return usdToTarget / usdToSource;
            }
        }
    }
    
}
