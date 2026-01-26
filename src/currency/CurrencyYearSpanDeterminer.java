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
    
    // TODO: Write tests for this
    public static YearSpan determineYearSpan(Currency currency) {
        String input = currency.getDisplayName();
        Matcher matcher = YEAR_SPAN_PATTERN.matcher(input);
        if (matcher.find()) {
            String s = matcher.group();
            return YearSpan.parse(s);
        }
        if (currency.getCurrencyCode().equals("ADP")) {
            Year begin = Year.of(1936);
            Year end = Year.of(2002);
            return new YearSpan(begin, end);
        }
        Year begin = Year.now();
        Year end = Year.of(2400);
        return new YearSpan(begin, end);
    }
    
}
