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
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Represents a pair of currencies. Preferably two distinct currencies. This is 
 * an immutable class. Its main purpose is to facilitate caching of recent 
 * results from a foreign exchange API.
 * @author Alonso del Arte
 */
public class CurrencyPair {
    
    private static final String DIRECTION_WORD_KEY = "directionToWord";
    
    private static final String INCLUDE_SPACES_KEY = "includeSpaces";
    
    private final Currency source, target;
    
    /**
     * Retrieves the From currency given to the constructor.
     * @return The From currency. For example, United States dollars (USD).
     */
    public Currency getFromCurrency() {
        return this.source;
    }
    
    /**
     * Retrieves the To currency given to the constructor.
     * @return The To currency. For example, euros (EUR).
     */
    public Currency getToCurrency() {
        return this.target;
    }
    
    /**
     * Flips this pair of currencies. The From currency becomes the To currency, 
     * and the To currency becomes the From currency. For the example, let's say 
     * this pair is from United States dollars (USD) to euros (EUR). 
     * @return A flipped currency pair. For example, from EUR to USD.
     */
    public CurrencyPair flip() {
        return new CurrencyPair(this.target, this.source);
    }
    
    /**
     * Gives a textual representation of this currency pair. This is designed to 
     * be easy to put in a query to Manny's currency conversion API. For the 
     * example, let's say this pair is United States dollars (USD) and euros 
     * (EUR).
     * @return The ISO-4217 letter codes for the From and To currencies, 
     * separated by an underscore. For example, "USD_EUR".
     */
    @Override
    public String toString() {
        return this.source.getCurrencyCode() + '_' 
                + this.target.getCurrencyCode();
    }
    
    /**
     * Gives text to describe this currency pair in the default locale, provided 
     * the necessary elements are available in the Java runtime's currency 
     * information file and this program's internationalization files. If either 
     * of those is lacking (the latter, almost certainly), fallbacks, most 
     * likely in English, will be used. For the example, suppose this currency 
     * pair is United States dollars (USD) to Jordanian dinars (JOD), and the 
     * default locale is {@code Locale.GERMAN}.
     * @return Text suitable for the locale, or some text with fallbacks to 
     * English. In the example, this would be "US-Dollar zu Jordanischer Dinar".
     */
    public String toDisplayString() {
        return this.toDisplayString(Locale.getDefault());
    }
    
    /**
     * Gives text to describe this currency pair in the specified locale, 
     * provided the necessary elements are available in the Java runtime's 
     * currency information file and this program's internationalization files. 
     * If either of those is lacking (the latter, almost certainly), fallbacks, 
     * most likely in English, will be used. For the example, suppose this 
     * currency pair is United States dollars (USD) to Jordanian dinars (JOD).
     * @param locale The locale. For example, {@code Locale.GERMAN}.
     * @return Text suitable for the locale, or some text with fallbacks to 
     * English. In the example, this would be "US-Dollar zu Jordanischer Dinar".
     */
    public String toDisplayString(Locale locale) {
        String fromName = this.source.getDisplayName(locale);
        String toName = this.target.getDisplayName(locale);
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.uiLabels", 
                locale);
        String dirWord = bundle.getString(DIRECTION_WORD_KEY);
        String inclSpStr = bundle.getString(INCLUDE_SPACES_KEY);
        boolean inclSpaces = Boolean.parseBoolean(inclSpStr);
        String connector = (inclSpaces) ? ' ' + dirWord + ' ' : dirWord;
        return fromName + connector + toName;
    }
    
    /**
     * Determines if this currency pair is equal to another object. For the 
     * examples, suppose this currency pair is from United States dollars (USD) 
     * to euros (EUR).
     * @param obj The object to compare against. Examples: a currency pair from 
     * USD to EUR, a currency pair from USD to Canadian dollars (CAD), a 
     * currency pair from CAD to EUR, the {@code Currency} instance for CAD, and 
     * a null.
     * @return True if {@code obj} matches this currency pair in both the From 
     * and To currencies, false in all other cases. In the examples, this would 
     * be true for the USD to EUR currency pair, false for the USD to CAD pair 
     * (To currencies don't match), false for the CAD to EUR pair (From 
     * currencies don't match), false for the CAD instance, and certainly false 
     * for the null.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }
        CurrencyPair other = (CurrencyPair) obj;
        if (!this.source.equals(other.source)) {
            return false;
        }
        return this.target.equals(other.target);
    }
    
    /**
     * Gives a hash code for this currency pair. The hash code is based on the 
     * 3-digit codes from ISO-4217. I reserve the right to change the 
     * mathematical formula in a later version of this class.
     * @return A hash code based on the From and To currencies' 3-digit codes in 
     * ISO-4217. For example, for a pair from United States dollars (USD) to 
     * euros (EUR), this might be 55051218 = 840 &times; 65536 + 978.
     */
    @Override
    public int hashCode() {
        return (this.source.getNumericCode() << 16) 
                + this.target.getNumericCode();
    }
    
    /**
     * Constructor.
     * @param from The From currency. For example, United States dollars (USD).
     * @param to The To currency. For example, euros (EUR). Ought to be 
     * different from {@code from}, but this is not checked.
     * @throws NullPointerException If either {@code from} or {@code to} is 
     * null.
     */
    public CurrencyPair(Currency from, Currency to) {
        if (from == null || to == null) {
            String excMsg = "From and To currencies should not be null";
            throw new NullPointerException(excMsg);
        }
        this.source = from;
        this.target = to;
    }
    
}
