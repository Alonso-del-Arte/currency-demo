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

import java.time.LocalDate;
import java.time.Month;
import java.util.Currency;

/**
 *
 * @author Alonso del Arte
 */
public class HardCodedRateProvider implements ExchangeRateProvider {
    
    /**
     * Gives the date that the values given by this provider were hard-coded on. 
     * It is expected that some of the values will soon go stale, while others 
     * might remain current until the next hard-coding.
     */
    public static final LocalDate DATE_OF_HARD_CODING 
            = LocalDate.of(2025, Month.MARCH, 3);
    
    @Override
    public double getRate(Currency source, Currency target) {
        return switch(target.getCurrencyCode()) {
            case "AUD" -> 1.6116;
            case "BRL" -> 5.9909941;
            case "CAD" -> 1.45;
            case "CNY" -> 7.29;
            case "EUR" -> 0.95;
            case "GBP" -> 0.78822;
            case "HKD" -> 7.78;
            case "ILS" -> 3.6;
            case "JPY" -> 149.22;
            case "KRW" -> 1459.18;
            case "MXN" -> 20.78;
            case "NZD" -> 1.78;
            case "PHP" -> 57.79;
            case "TWD" -> 32.92;
            case "USD" -> 1.0;
            case "VND" -> 25589.98;
            case "XAF" -> 625.4;
            case "XCD" -> 2.7;
            case "XOF" -> 625.39;
            case "XPF" -> 113.71;
            default -> -1.0;
        };
    }
    
}
