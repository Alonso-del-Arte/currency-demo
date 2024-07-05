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

import currency.CurrencyConverter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.Currency;

/** WORK IN PROGRESS...
 * Compares currencies according to their exchange rates. A connection to the  
 * online currency conversion API used by {@link CurrencyConverter} is required.
 * @author Alonso del Arte
 */
public class ExchangeRateComparator implements Comparator<Currency> {
    
    // TODO: Write tests for this
    @Override
    public int compare(Currency currencyA, Currency currencyB) {
        return 0;
    }
    
    /**
     * Constructor.
     * @param base The currency on which to base the comparisons. For example, 
     * the United States dollar (USD).
     */
    public ExchangeRateComparator(Currency base) {
        // TODO: Write tests for this
    }
  
}
