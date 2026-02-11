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

import java.util.Comparator;
import java.util.Currency;

import timeops.YearSpan;

/**
 * Compares currencies according to their year spans of validity, prioritizing 
 * currencies that are no longer active. Some historical currencies, such as the 
 * old Sudanese dinar (SDD), have their years of validity (for example, 1992 
 * &mdash; 2007, in the case of the old Sudanese dinar) listed in Java's 
 * currency information file. This is not the case with euro-replaced 
 * currencies.
 * @author Alonso del Arte
 */
public class HistoricalCurrenciesComparator implements Comparator<Currency> {
    
    /**
     * Compares two currencies according to their years of validity. The years 
     * of validity are gleaned from the Java currency information file when 
     * possible. For euro-replaced currencies, the years of validity have been 
     * "hard-coded" in this program. Currently active currencies like the United 
     * States dollar (USD) are considered to have a range of validity starting 
     * with the present year. This is so that currently active currencies will 
     * be sorted after historical currencies.
     * @param currencyA The first currency to compare. For example, the present 
     * day Venezuelan bol&iacute;var (VES).
     * @param currencyB The second currency to compare. For example, the 
     * Venezuelan bol&iacute;var from 1871 to 2008 (VEB).
     * @return &minus;1 or any other negative integer if {@code currencyA} is 
     * historical and {@code currencyB} is a currently active currency or if 
     * both currencies are historical but {@code currencyA} has a span of years 
     * of validity beginning prior to those of {@code currencyB}; 0 if both 
     * currencies are currently active currencies, or, less likely, both are 
     * historical currencies with the exact same years of validity; 1 or any 
     * other positive integer if {@code currencyA} is a currently active 
     * currency and {@code currencyB} is historical, or if both are historical 
     * but {@code currencyA} has a span of years of validity beginning after 
     * those of {@code currencyB}. In the example with VES and VEB, the result 
     * could be 1 or 155 or some other positive integer, since VES is currently 
     * active while VEB was valid from 1871 to 2008.
     */
    @Override
    public int compare(Currency currencyA, Currency currencyB) {
        YearSpan spanA = determineYearSpan(currencyA);
        YearSpan spanB = determineYearSpan(currencyB);
        return spanA.compareTo(spanB);
    }
    
}
