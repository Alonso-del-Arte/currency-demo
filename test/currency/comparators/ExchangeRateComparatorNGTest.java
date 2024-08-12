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
import currency.MoneyAmount;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the ExchangeRateComparator class.
 * @author Alonso del Arte
 */
public class ExchangeRateComparatorNGTest {
    
    private static final List<String> CURRENCY_CODES = List.of("AUD", "CAD", 
            "CHF", "EUR", "GBP", "JPY", "USD");
    
    private static final List<Currency> CURRENCIES = CURRENCY_CODES.stream()
            .map((s) -> Currency.getInstance(s)).collect(Collectors.toList());
    
    private static List<Currency> makeList(Currency currency) {
        Map<MoneyAmount, Currency> map = new TreeMap<>();
        MoneyAmount baseAmount = new MoneyAmount(1, currency);
        List<Currency> list = new ArrayList<>();
        return list;
    }
    
    /**
     * Test of the compare function, of the ExchangeRateComparator class.
     */
    @Test
    public void testCompare() {
        System.out.println("compare");
        for (Currency currency : CURRENCIES) {
            System.out.println(currency.getDisplayName() + " (" 
                    + currency.getCurrencyCode() + ")");
        }
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
