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

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Test of the LetterCodeComparator class.
 * @author Alonso del Arte
 */
public class LetterCodeComparatorNGTest {
    
    private static final Set<Currency> ALL_CURRENCIES 
            = Currency.getAvailableCurrencies();
    
    @Test
    public void testCompare() {
        int initialCapacity = ALL_CURRENCIES.size();
        List<Currency> expected = new ArrayList<>(initialCapacity);
        List<Currency> actual = new ArrayList<>(ALL_CURRENCIES);
        String[] letterCodes = new String[initialCapacity];
        for (int i = 0; i < initialCapacity; i++) {
            letterCodes[i] = actual.get(i).getCurrencyCode();
        }
        Arrays.sort(letterCodes);
        for (int j = 0; j < initialCapacity; j++) {
            expected.add(Currency.getInstance(letterCodes[j]));
        }
        Collections.sort(actual, new LetterCodeComparator());
        assertEquals(actual, expected);
    }
    
}
