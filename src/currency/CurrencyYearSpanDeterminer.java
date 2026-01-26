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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timeops.YearSpan;

/**
 *
 * @author Alonso del Arte
 */
public class CurrencyYearSpanDeterminer {
    
    private static final Pattern YEAR_SPAN_PATTERN 
            = Pattern.compile("\\d{4}.\\d{4}");
    
    private static final Year EURO_YEAR_ZERO = Year.of(2002);
    
    private static final Map<Currency, YearSpan> EURO_REPLACED_YEAR_SPANS 
            = new HashMap<>();
    
    static {
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("ADP"), 
                new YearSpan(Year.of(1936), EURO_YEAR_ZERO));
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("ATS"), 
                new YearSpan(Year.of(1945), EURO_YEAR_ZERO));
    }
    
    // TODO: Write tests for this
    public static YearSpan determineYearSpan(Currency currency) {
        String input = currency.getDisplayName();
        Matcher matcher = YEAR_SPAN_PATTERN.matcher(input);
        if (matcher.find()) {
            String s = matcher.group();
            return YearSpan.parse(s);
        }
        if (EURO_REPLACED_YEAR_SPANS.containsKey(currency)) {
            return EURO_REPLACED_YEAR_SPANS.get(currency);
        }
        Year begin = Year.now();
        Year end = Year.of(2400);
        return new YearSpan(begin, end);
    }
    
}
