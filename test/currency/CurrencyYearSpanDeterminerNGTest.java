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
    
    private static final Year EURO_YEAR_ZERO = Year.of(2002);
    
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
    
    @Test
    public void testDetermineYearSpanActiveCurrency() {
        Currency currency = CurrencyChooser.chooseCurrency();
        YearSpan span = CurrencyYearSpanDeterminer.determineYearSpan(currency);
        Year expected = Year.now();
        Year actual = span.getBeginYear();
        String message = "Since currency " + currency.getDisplayName() + " (" 
                + currency.getCurrencyCode() 
                + ") is an active currency, its year span should begin now";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testDetermineYearSpanAndorranPeseta() {
        Currency currency = Currency.getInstance("ADP");
        Year begin = Year.of(1936);
        YearSpan expected = new YearSpan(begin, EURO_YEAR_ZERO);
        YearSpan actual 
                = CurrencyYearSpanDeterminer.determineYearSpan(currency);
        String message = "Currency " + currency.getDisplayName() + " (" 
                + currency.getCurrencyCode() + ") is a euro-replaced currency";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testDetermineYearSpanAustrianSchilling() {
        Currency currency = Currency.getInstance("ATS");
        Year begin = Year.of(1945);
        YearSpan expected = new YearSpan(begin, EURO_YEAR_ZERO);
        YearSpan actual 
                = CurrencyYearSpanDeterminer.determineYearSpan(currency);
        String message = "Currency " + currency.getDisplayName() + " (" 
                + currency.getCurrencyCode() + ") is a euro-replaced currency";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testDetermineYearSpanBelgianFranc() {
        Currency currency = Currency.getInstance("BEF");
        Year begin = Year.of(1832);
        YearSpan expected = new YearSpan(begin, EURO_YEAR_ZERO);
        YearSpan actual 
                = CurrencyYearSpanDeterminer.determineYearSpan(currency);
        String message = "Currency " + currency.getDisplayName() + " (" 
                + currency.getCurrencyCode() + ") is a euro-replaced currency";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testDetermineYearSpanBulgarianLev() {
        Currency currency = Currency.getInstance("BGN");
        Year begin = Year.of(1880);
        Year end = Year.of(2026);
        YearSpan expected = new YearSpan(begin, end);
        YearSpan actual 
                = CurrencyYearSpanDeterminer.determineYearSpan(currency);
        String message = "Currency " + currency.getDisplayName() + " (" 
                + currency.getCurrencyCode() + ") is a euro-replaced currency";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testDetermineYearSpanCypriotPound() {
        Currency currency = Currency.getInstance("CYP");
        Year begin = Year.of(1879);
        Year end = Year.of(2008);
        YearSpan expected = new YearSpan(begin, end);
        YearSpan actual 
                = CurrencyYearSpanDeterminer.determineYearSpan(currency);
        String message = "Currency " + currency.getDisplayName() + " (" 
                + currency.getCurrencyCode() + ") is a euro-replaced currency";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testDetermineYearSpanGermanMark() {
        Currency currency = Currency.getInstance("DEM");
        Year begin = Year.of(1990);
        YearSpan expected = new YearSpan(begin, EURO_YEAR_ZERO);
        YearSpan actual 
                = CurrencyYearSpanDeterminer.determineYearSpan(currency);
        String message = "Currency " + currency.getDisplayName() + " (" 
                + currency.getCurrencyCode() + ") is a euro-replaced currency";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testDetermineYearSpanEstonianKroon() {
        Currency currency = Currency.getInstance("EEK");
        Year begin = Year.of(1992);
        Year end = Year.of(2011);
        YearSpan expected = new YearSpan(begin, end);
        YearSpan actual 
                = CurrencyYearSpanDeterminer.determineYearSpan(currency);
        String message = "Currency " + currency.getDisplayName() + " (" 
                + currency.getCurrencyCode() + ") is a euro-replaced currency";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testDetermineYearSpanSpanishPeseta() {
        Currency currency = Currency.getInstance("ESP");
        Year begin = Year.of(1868);
        YearSpan expected = new YearSpan(begin, EURO_YEAR_ZERO);
        YearSpan actual 
                = CurrencyYearSpanDeterminer.determineYearSpan(currency);
        String message = "Currency " + currency.getDisplayName() + " (" 
                + currency.getCurrencyCode() + ") is a euro-replaced currency";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testDetermineYearSpanFinnishMarkka() {
        Currency currency = Currency.getInstance("FIM");
        Year begin = Year.of(1860);
        YearSpan expected = new YearSpan(begin, EURO_YEAR_ZERO);
        YearSpan actual 
                = CurrencyYearSpanDeterminer.determineYearSpan(currency);
        String message = "Currency " + currency.getDisplayName() + " (" 
                + currency.getCurrencyCode() + ") is a euro-replaced currency";
        assertEquals(actual, expected, message);
    }
    
}
