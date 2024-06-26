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
 * Compares currencies according to their 3-digit codes (zero-padded as needed) 
 * in the ISO-4217 standard. To compare currencies according to their 3-letter 
 * codes, use {@link LetterCodeComparator}.
 * <p>For reference, the French gold franc (XFO) has numeric code 000, and the 
 * unknown currency (XXX) has numeric code 999.</p>
 * @author Alonso del Arte
 */
public class NumericCodeComparator implements Comparator<Currency> {
    
    /**
     * Compares currencies according to their 3-digit codes in the ISO-4217 
     * standard. A currency's 3-digit code is given by the <code>Currency</code>
     * instance's <code>getNumericCode()</code> function.
     * @param currencyA The first currency to compare. For example, the Armenian 
     * dram (AMD), which has numeric code 051.
     * @param currencyB The second currency to compare. For example, the South 
     * African rand (ZAR), which has numeric code 710.
     * @return &minus;1 or less if <code>currencyA</code> has a 
     * lower 3-digit currency code than <code>currencyB</code>, 1 or greater if 
     * <code>currencyA</code> has a higher 3-digit currency code than 
     * <code>currencyB</code>, or 0 if <code>currencyA</code> and 
     * <code>currencyB</code> are the same currency. In the example with the 
     * Armenian dram (051) for <code>currencyA</code> and the South African rand 
     * (710) for <code>currencyB</code>, this function will return &minus;1, 
     * &minus;659 or any other negative integer. If we switch these two example 
     * currencies, this function will return 1, 659 or any other positive 
     * integer.
     */
    @Override
    public int compare(Currency currencyA, Currency currencyB) {
        return Integer.compare(currencyA.getNumericCode(), 
                currencyB.getNumericCode());
    }
  
}
