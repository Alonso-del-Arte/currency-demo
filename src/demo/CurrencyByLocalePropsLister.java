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
package demo;

import java.util.Currency;
import java.util.Locale;

/**
 * Displays information about various currencies according to their locales. The 
 * available currencies are those recognized by the Java runtime through the 
 * {@link java.util.Currency} class, and the available locales are those 
 * recognized by the Java runtime through the {@link java.util.Locale} class.
 * @author Alonso del Arte
 */
public class CurrencyByLocalePropsLister {
    
    /**
     * Writes to the console information about the currency associated with a 
     * given locale. If the locale does not have any associated currency, a 
     * message to that effect will be written to the console.
     * @param locale The locale for which the currency information is to be 
     * printed. Two examples: Samburu (Kenya) which is associated with the 
     * Kenyan shilling (KES) and Central Kurdish which is not associated with 
     * any currency.
     */
    static void printCurrencyInfo(Locale locale) {
        try {
            Currency currency = Currency.getInstance(locale);
            System.out.print(currency.getDisplayName());
            System.out.println("            Symbol: " + currency.getSymbol());
            System.out.print("Symbol for locale " + locale.getDisplayName());
            System.out.println(": " + currency.getSymbol(locale));
            System.out.print("ISO 4217 letter code: " 
                    + currency.getCurrencyCode());
            System.out.println("ISO 4217 number code: " 
                    + currency.getNumericCode());
            System.out.println("Default fraction digits: " 
                    + currency.getDefaultFractionDigits());
            System.out.println();
        } catch (IllegalArgumentException iae) {
            System.out.println("There is no currency for locale " 
                    + locale.getDisplayName());
            System.out.println("\"" + iae.getMessage() + "\"");
            System.out.println();
        }
    }
    
    // TODO: Write tests for this
    public static void main(String[] args) {
        printCurrencyInfo(Locale.getDefault());
//        Locale defaultLocale = Locale.getDefault();
//        System.out.println("Your locale is " + defaultLocale.toString());
//        System.out.println();
//        Locale[] locales = Locale.getAvailableLocales();
//        for (Locale locale : locales) {
//            printCurrencyInfo(locale);
//        }
    }
    
}
