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
package ui;

import java.util.Currency;

/**
 * Wraps a {@code Currency} object. The purpose of this is to provide a 
 * friendlier way to select a currency from a dropdown menu, so that it's not as 
 * frequently necessary to look up a currency's 3-letter or 3-digit code in the 
 * ISO-4217 standard.
 * @author Alonso del Arte
 */
public class CurrencyWrapper {
    
    private final Currency wrappedCurrency;
    
    /**
     * Provides a textual representation of the wrapped currency of more than 
     * just the 3-letter or 3-digit ISO-4217 code. For the example, suppose this 
     * instance wraps the British pound (GBP).
     * @return The 3-letter ISO-4217 code followed by a space and an em dash, 
     * followed by the display name in the current locale, followed by a space 
     * and the 3-digit ISO-4217 code in parentheses. For example "GBP &mdash; 
     * British pound (826)".
     */
    @Override
    public String toString() {
        return this.wrappedCurrency.getCurrencyCode() + " \u2014 " 
                + this.wrappedCurrency.getDisplayName() + " (" 
                + this.wrappedCurrency.getNumericCodeAsString() + ")";
    }
    
    public CurrencyWrapper(Currency currency) {
        this.wrappedCurrency = currency;
    }
    
}
