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
package currency;

import java.time.Year;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

import timeops.YearSpan;

/**
 * Tests of the CurrencyYearSpanDeterminer class.
 * @author Alonso del Arte
 */
public class CurrencyYearSpanDeterminerNGTest {
    
    private static final Set<Currency> CURRENCIES 
            = Currency.getAvailableCurrencies();
    
    private static final Pattern YEAR_SPAN_PATTERN 
            = Pattern.compile("\\d{4}.\\d{4}");
    
    private static boolean hasYearSpanIndicated(Currency cur) {
        String input = cur.getDisplayName();
        Matcher matcher = YEAR_SPAN_PATTERN.matcher(input);
        return matcher.find();
    }
    
    private static final Set<Currency> CURRENCIES_YEAR_MARKED 
            = CURRENCIES.stream().filter(cur -> hasYearSpanIndicated(cur))
            .collect(Collectors.toSet());
    
    /**
     * Test of the determineYearSpan function, of the CurrencyYearSpanDeterminer 
     * class.
     */
    @Test
    public void testDetermineYearSpan() {
        System.out.println("determineYearSpan");
        for (Currency currency : CURRENCIES_YEAR_MARKED) {
            String input = currency.getDisplayName();
            Matcher matcher = YEAR_SPAN_PATTERN.matcher(input);
            matcher.find();
            String s = matcher.group();
            YearSpan expected = YearSpan.parse(s);
            YearSpan actual 
                    = CurrencyYearSpanDeterminer.determineYearSpan(currency);
            String message = "Reckoning year span for " + input;
            assertEquals(actual, expected, message);
        }
    }
    
    
}
