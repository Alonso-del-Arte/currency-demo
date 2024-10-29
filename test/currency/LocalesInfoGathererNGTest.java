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
package currency;

import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the LocalesInfoGatherer class.
 * @author Alonso del Arte
 */
public class LocalesInfoGathererNGTest {
    
    private static final Locale[] LOCALES = Locale.getAvailableLocales();
    
    /**
     * Test of the getCurrency function, of the LocalesInfoGatherer class.
     */
    @Test
    public void testGetCurrency() {
        System.out.println("getCurrency");
        Currency expected = CurrencyChooser.chooseCurrency();
        LocalesInfoGatherer instance = new LocalesInfoGatherer(expected);
        Currency actual = instance.getCurrency();
        assertEquals(actual, expected);
    }
    
    private static Map<String, Set<Locale>> gatherSymbols(Currency currency) {
        Map<String, Set<Locale>> map = new HashMap<>();
        for (Locale locale : LOCALES) {
            String symbol = currency.getSymbol(locale);
            if (map.containsKey(symbol)) {
                Set<Locale> set = map.get(symbol);
                set.add(locale);
            } else {
                Set<Locale> set = new HashSet<>();
                set.add(locale);
                map.put(symbol, set);
            }
        }
        return map;
    }

    /**
     * Test of the getSymbols function, of the LocalesInfoGatherer class.
     */
    @Test
    public void testGetSymbols() {
        System.out.println("getSymbols");
        Currency currency = CurrencyChooser.chooseCurrency();
        LocalesInfoGatherer instance = new LocalesInfoGatherer(currency);
        Map<String, Set<Locale>> expected = gatherSymbols(currency);
        Map<String, Set<Locale>> actual = instance.getSymbols();
        String message = "Gathering symbols for " + currency.getDisplayName() 
                + " (" + currency.getCurrencyCode() + ")";
        assertEquals(actual, expected, message);
    }

    /**
     * Test of getDisplayNames method, of class LocalesInfoGatherer.
     */
    @Test
    public void testGetDisplayNames() {
        System.out.println("getDisplayNames");
//        LocalesInfoGatherer instance = null;
//        Map expResult = null;
//        Map result = instance.getDisplayNames();
//        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
