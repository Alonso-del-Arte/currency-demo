/*
 * Copyright (C) 2024 Alonso del Arte
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
package currency.comparators;

import java.util.Comparator;
import java.util.Currency;

/**
 * Compares currencies according to their 3-letter codes in the ISO-4217 
 * standard. To compare currencies according to their 3-digit codes in the 
 * ISO-4217 standard, use {@link NumericCodeComparator}.
 * @author Alonso del Arte
 */
public class LetterCodeComparator implements Comparator<Currency> {
    
    /**
     * Compares currencies according to their 3-letter codes in the ISO-4217
     * standard. A currency's 3-letter code is given by the {@code Currency}
     * instance's {@code getCurrencyCode()} function.
     * @param currencyA The first currency to compare. For example, the Armenian
     * dram (AMD).
     * @param currencyB The second currency to compare. For example, the South
     * African rand (ZAR).
     * @return &minus;1 or less if {@code currencyA} has a lexicographically
     * earlier 3-letter currency code than {@code currencyB}, 1 or greater if
     * {@code currencyA} has a lexicographically later 3-letter currency code
     * than {@code currencyB}, or 0 if and only if {@code currencyA} and {@code
     * currencyB} are the same currency. In the example with the Armenian dram
     * (AMD) for {@code currencyA} and the South African rand for {@code
     * currencyB}, this function will return &minus;1 or &minus;25 or any other
     * negative integer. If we switch these two example currencies, this
     * function will return 1 or 25 or any other positive integer.
     * @throws NullPointerException If either {@code currencyA} or {@code
     * currencyB} is null.
     */
    @Override
    public int compare(Currency currencyA, Currency currencyB) {
        return currencyA.getCurrencyCode()
                .compareTo(currencyB.getCurrencyCode());
    }
  
}
