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
 * Matches up currencies to year spans. If the year span is listed in the 
 * display name, that's extracted. For euro-replaced currencies, the year spans 
 * are "hard-coded" by this program.
 * @author Alonso del Arte
 */
public class CurrencyYearSpanDeterminer {
    
    private static final Pattern YEAR_SPAN_PATTERN 
            = Pattern.compile("\\d{4}.\\d{4}");
    
    private static final Year EURO_YEAR_ZERO = Year.of(2002);
    
    private static final Map<Currency, YearSpan> EURO_REPLACED_YEAR_SPANS 
            = new HashMap<>();
    
    // TODO: Refactor, maybe as a CSV file access
    static {
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("ADP"), 
                new YearSpan(Year.of(1936), EURO_YEAR_ZERO));
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("ATS"), 
                new YearSpan(Year.of(1945), EURO_YEAR_ZERO));
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("BEF"), 
                new YearSpan(Year.of(1832), EURO_YEAR_ZERO));
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("BGN"), 
                new YearSpan(Year.of(1880), Year.of(2026)));
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("CYP"), 
                new YearSpan(Year.of(1879), Year.of(2008)));
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("DEM"), 
                new YearSpan(Year.of(1990), EURO_YEAR_ZERO));
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("EEK"), 
                new YearSpan(Year.of(1992), Year.of(2011)));
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("ESP"), 
                new YearSpan(Year.of(1868), EURO_YEAR_ZERO));
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("FIM"), 
                new YearSpan(Year.of(1860), EURO_YEAR_ZERO));
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("FRF"), 
                new YearSpan(Year.of(1960), EURO_YEAR_ZERO));
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("GRD"), 
                new YearSpan(Year.of(1833), EURO_YEAR_ZERO));
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("IEP"), 
                new YearSpan(Year.of(1928), EURO_YEAR_ZERO));
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("ITL"), 
                new YearSpan(Year.of(1861), EURO_YEAR_ZERO));
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("LUF"), 
                new YearSpan(Year.of(1854), EURO_YEAR_ZERO));
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("MTL"), 
                new YearSpan(Year.of(1972), Year.of(2008)));
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("NLG"), 
                new YearSpan(Year.of(1434), EURO_YEAR_ZERO));
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("PTE"), 
                new YearSpan(Year.of(1911), EURO_YEAR_ZERO));
        EURO_REPLACED_YEAR_SPANS.put(Currency.getInstance("SIT"), 
                new YearSpan(Year.of(1991), Year.of(2007)));
    }
    
    /**
     * Determines year spans for currencies.
     * @param currency The currency for which to determine the year span. 
     * Examples: Dutch guilder (NLG)
     * @return The year span
     */
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
