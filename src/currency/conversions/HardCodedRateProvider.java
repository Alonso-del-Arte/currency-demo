/*
 * Copyright (C) 2026 Alonso del Arte
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
 * for the date that these values were hard-coded. This is to be used for 
 * demonstrations for which the most up-to-date values are not available, or are 
 * not relevant to what is being demonstrated.
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
            = LocalDate.of(2026, Month.FEBRUARY, 24);
    
    private static final Currency UNITED_STATES_DOLLARS 
            = Currency.getInstance(Locale.US);
    
    private static final String[] CURRENCY_CODES = {"AUD", "BRL", "CAD", "CNY", 
        "EUR", "GBP", "HKD", "ILS", "INR", "IRR", "JPY", "KRW", "KWD", "LBP", 
        "MXN", "NZD", "PHP", "TWD", "USD", "VND", "XAF", "XCD", "XOF", "XPF"};
    
    private static final Set<Currency> SUPPORTED_CURRENCIES 
            = Set.of(CURRENCY_CODES).stream().map(
                    currencyCode -> Currency.getInstance(currencyCode)
            ).collect(Collectors.toSet());
    
    // TODO: Figure out a better way to attach rates
    private static final double[] HARD_CODED_RATES = {1.4158, 5.1764, 1.3687, 
        6.9163, 0.8481, 0.7409, 7.8188, 3.1229, 90.9674, 1284780.8693, 154.5984, 
        1443.8242, 0.3066, 89500.0, 17.2415, 1.6773, 57.6602, 31.4496, 1.0, 
        26034.7402, 556.3434, 2.7,556.3434, 101.2103};
    
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
    
    private static void checkSupport(Currency source, Currency target) {
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
    }
    
    private static double validatedPairGetRate(CurrencyPair currencies) {
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
                double usdToSource = QUOTES_MAP.get(sourcePair);
                double usdToTarget = QUOTES_MAP.get(targetPair);
                double value = usdToTarget / usdToSource;
                QUOTES_MAP.put(currencies, value);
                return value;
            }
        }
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
     * @throws NoSuchElementException If either {@code source} or {@code target} 
     * is not among the supported currencies. See {@link 
     * #supportedCurrencies()}.
     */
    @Override
    public double getRate(Currency source, Currency target) {
        checkSupport(source, target);
        CurrencyPair currencies = new CurrencyPair(source, target);
        return validatedPairGetRate(currencies);
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
     * @throws NoSuchElementException If either of the currencies of {@code 
     * currencies} are not supported. See {@link #supportedCurrencies()}.
     */
    @Override
    public double getRate(CurrencyPair currencies) {
        checkSupport(currencies.getFromCurrency(), currencies.getToCurrency());
        return validatedPairGetRate(currencies);
    }
    
}
