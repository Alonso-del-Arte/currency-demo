/*
 * Copyright (C) 2025 Alonso del Arte
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

import java.util.Currency;
import java.util.Set;

/**
 * Indicates that a class can support specific instances of {@code Currency} but 
 * not others. For the set of supported currencies, call {@link 
 * #supportedCurrencies()}.
 * @author al
 */
public interface SpecificCurrenciesSupport {
    
    /**
     * The currencies that are supported.
     * @return A set of currencies. For example, a set containing United States 
     * dollars (USD), euros (EUR) and the rest of the top 20 most traded 
     * currencies in the world.
     */
    Set<Currency> supportedCurrencies();
    
}
