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
 *
 * @author Alonso del Arte
 */
public class HistoricalCurrenciesComparator implements Comparator<Currency> {
    
    @Override
    public int compare(Currency currencyA, Currency currencyB) {
        YearSpan spanA = determineYearSpan(currencyA);
        YearSpan spanB = determineYearSpan(currencyB);
        return spanA.compareTo(spanB);
    }
    
}
