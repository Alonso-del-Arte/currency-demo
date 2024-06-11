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
import java.util.Set;

/**
 * Displays information about various currencies. The available currencies are 
 * those recognized by the Java runtime through the {@link java.util.Currency} 
 * class.
 * @author Alonso del Arte
 */
public class CurrencyPropsLister {
    
    static void printCurrencyInfo(Currency currency) {
        System.out.print(currency.getDisplayName());
        System.out.print("   Symbol: " + currency.getSymbol());
        System.out.println("   ISO 4217: " + currency.getCurrencyCode());
        System.out.print("Number code: " + currency.getNumericCode());
        System.out.println("   Default fraction digits: " 
                + currency.getDefaultFractionDigits());
        System.out.println();
    }
    
    /**
     * Displays information about specified currencies, or all currencies if 
     * none are specified. This program only recognizes currencies recognized by 
     * <code>java.util.Currency</code>. For each currency, this program displays 
     * a locale-specific symbol (might match the ISO-4217 letter code), the 
     * ISO-4217 letter code, the ISO-4217 number code and the default fraction 
     * digits (such as 2 for currencies that divide into cents).
     * @param args The ISO-4217 letter codes for the desired currencies. For 
     * example, {"USD", "EUR", "JPY"} for the United States dollar, the euro and 
     * the Japanese yen. May be an empty array, in which case all currencies 
     * recognized by <code>java.util.Currency</code> will be displayed.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            Set<Currency> currencies = Currency.getAvailableCurrencies();
            System.out.println("There are " + currencies.size() 
                    + " currencies:");
            currencies.forEach((currency) -> {
                printCurrencyInfo(currency);
            });
        } else {
            for (String arg : args) {
                try {
                    Currency currency = Currency.getInstance(arg);
                    printCurrencyInfo(currency);
                } catch (IllegalArgumentException iae) {
                    System.out.println(arg + " is not a valid currency code");
                    System.out.println("\"" + iae.getMessage() + "\"");
                    System.out.println();
                }
            }
        }
    }
    
}
