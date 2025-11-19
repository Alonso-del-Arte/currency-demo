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

/**
 * Exception for when a particular currency is not supported by an instance of a 
 * given class. That class should probably implement the {@link 
 * SpecificCurrenciesSupport} interface.
 * <p>This exception should not be used in cases where two currencies are 
 * mismatched, such as trying to add an amount of one currency to an amount of 
 * another currency. For that, use {@link CurrencyMismatchException}.</p>
 * @author Alonso del Arte
 */
public class UnsupportedCurrencyException extends RuntimeException {
    
    private final Currency heldCurrency;
    
    /**
     * Retrieves the currency this exception was constructed with.
     * @return The currency. For example, the North Korean won (KPW).
     */
    public Currency getCurrency() {
        return this.heldCurrency;
    }
    
    /**
     * Auxiliary constructor. It will fill in a message saying the specified 
     * currency is not supported. For a customized message, use {@link 
     * #UnsupportedCurrencyException(java.util.Currency, java.lang.String) the 
     * primary constructor}.
     * @param currency The currency that is not supported. For example, the 
     * Myanmar kyat (MMK).
     */
    public UnsupportedCurrencyException(Currency currency) {
        this(currency, "Currency " + currency.getDisplayName() + " (" 
                + currency.getCurrencyCode() + ") not supported");
    }
    
    /**
     * Primary constructor. If there is no need to give an explanation as to why 
     * the currency is not supported, use {@link 
     * #UnsupportedCurrencyException(java.util.Currency) the auxiliary 
     * constructor}.
     * @param currency The currency that is not supported. For example, RUR, the 
     * Russian ruble that was used for a few years after the collapse of the 
     * Soviet Union in 1991.
     * @param message An explanation as to why the currency is not supported. 
     * For example, "The specified currency is a historical currency".
     */
    public UnsupportedCurrencyException(Currency currency, String message) {
        super(message);
        this.heldCurrency = currency;
    }
    
}
