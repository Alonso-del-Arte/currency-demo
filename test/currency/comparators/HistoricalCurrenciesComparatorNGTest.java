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
package currency.comparators;

import static currency.CurrencyYearSpanDeterminer.determineYearSpan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.testframe.api.Asserters.assertContainsSameOrder;

import org.testng.annotations.Test;

import timeops.YearSpan;

/**
 * Tests of the HistoricalCurrenciesComparator class.
 * @author Alonso del Arte
 */
public class HistoricalCurrenciesComparatorNGTest {
    
    /**
     * Test of the compare function, of the HistoricalCurrenciesComparator 
     * class.
     */
    @Test
    public void testCompare() {
        System.out.println("compare");
        Set<Currency> currencies = Currency.getAvailableCurrencies();
        List<Currency> actual 
                = currencies.stream().collect(Collectors.toList());
        List<Currency> expected = new ArrayList<>(actual);
        Collections.sort(expected, (Currency curA, Currency curB) -> {
            YearSpan spanA = determineYearSpan(curA);
            YearSpan spanB = determineYearSpan(curB);
            return spanA.compareTo(spanB);
        });
        Collections.sort(actual, new HistoricalCurrenciesComparator());
        String msg = "Currencies should be sorted according to year spans";
        assertContainsSameOrder(expected, actual, msg);
    }
    
}
