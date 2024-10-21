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
public final class CurrencyWrapper {
    
    private static final String EM_DASH_SPACED = " \u2014 ";
    
    private final Currency wrappedCurrency;
    
    /**
     * Gives the currency wrapped by this instance.
     * @return The currency passed to the constructor of this instance.
     */
    public Currency getWrappedCurrency() {
        return this.wrappedCurrency;
    }
    
    /**
     * Provides a textual representation of the wrapped currency of more than 
     * just the 3-letter or 3-digit ISO-4217 code. For the example, suppose this 
     * instance wraps the British pound (GBP).
     * @return The 3-letter ISO-4217 code followed by a space and an em dash, 
     * followed by the display name in the current locale, followed by a space 
     * and the 3-digit ISO-4217 code in parentheses. For example "GBP &mdash; 
     * &pound; &mdash; British pound (826)".
     */
    @Override
    public String toString() {
        String iso4217Code = this.wrappedCurrency.getCurrencyCode();
        String symbolStr = this.wrappedCurrency.getSymbol();
        String symbolInclude = iso4217Code.equals(symbolStr) 
                ? "" : symbolStr + EM_DASH_SPACED;
        return iso4217Code + EM_DASH_SPACED + symbolInclude 
                + this.wrappedCurrency.getDisplayName() + " (" 
                + this.wrappedCurrency.getNumericCodeAsString()+ ")";
    }
    
    /**
     * Determines if this wrapper is equal to some other object. For the 
     * examples, suppose this wrapper wraps the British pound (GBP).
     * @param obj The object to check against. Examples: wrapper for GBP, 
     * wrapper for the euro (EUR), the {@code Currency} instance for GBP, null.
     * @return True if this wrapper wraps the same {@code Currency} instance as 
     * {@code obj}, false in all other cases. In the examples, true for the 
     * wrapper for GBP, false for the wrapper for EUR (wraps a different  
     * currency), false for the {@code Currency} instance for GBP (even though 
     * that's the currency the example wrapper wraps) and obviously false for 
     * null.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CurrencyWrapper)) {
            return false;
        }
        return this.wrappedCurrency
                .equals(((CurrencyWrapper) obj).wrappedCurrency);
    }
    
    /**
     * Gives a hash code for this wrapper. The hash code is arithmetically 
     * related to the hash code for the wrapped currency in some way. Those two 
     * hash codes may or may not match for sign and parity.
     * @return A hash code. For example, if the {@code Currency} instance for 
     * euros (EUR) hashes to 1560911714, the wrapper might hash to 1173143867.
     */
    @Override
    public int hashCode() {
        return ((~this.wrappedCurrency.hashCode()) << 1) + 1;
    }
    
    // TODO: Write tests for this
    public static CurrencyWrapper[] wrap(Currency[] currencies) {
        int size = 4;
        if (currencies != null) {
            size = currencies.length;
        }
        CurrencyWrapper[] array = new CurrencyWrapper[size];
        CurrencyWrapper placeholder 
                = new CurrencyWrapper(Currency.getInstance("XTS"));
        java.util.Arrays.fill(array, placeholder);
        return array;
    }
    
    /**
     * Sole constructor.
     * @param currency The currency to wrap. For example, United States dollars 
     * (USD).
     */
    public CurrencyWrapper(Currency currency) {
        this.wrappedCurrency = currency;
    }
    
}
