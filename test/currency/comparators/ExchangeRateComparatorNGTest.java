/*
 * Copyright (C) 2026 Alonso del Arte
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

import currency.CurrencyChooser;
import currency.MoneyAmount;
import currency.conversions.CurrencyConverter;
import currency.conversions.ExchangeRateProvider;
import currency.conversions.HardCodedRateProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.testframe.api.Asserters.assertContainsSameOrder;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the ExchangeRateComparator class.
 * @author Alonso del Arte
 */
public class ExchangeRateComparatorNGTest {
    
    /**
     * Test of the compare function, of the ExchangeRateComparator class.
     */
    @Test
    public void testCompare() {
        System.out.println("compare");
        HardCodedRateProvider rateProvider = new HardCodedRateProvider();
        Set<Currency> currencies = rateProvider.supportedCurrencies();
        List<Currency> actual = currencies.stream()
                .collect(Collectors.toList());
        List<Currency> expected = new ArrayList<>(actual);
        Currency base = CurrencyChooser.chooseCurrency(currencies);
        Collections.sort(expected, new DraftComparator(base, rateProvider));
        Comparator<Currency> instance = new ExchangeRateComparator(base, 
                rateProvider);
        Collections.sort(actual, instance);
        String msg = "Expecting to sort by exchange rate to base currency " 
                + base.getDisplayName() + " (" + base.getCurrencyCode() + ")";
        assertContainsSameOrder(expected, actual, msg);
    }
    
    private static class DraftComparator implements Comparator<Currency> {
        
        private final Currency baseCur;
        
        private final ExchangeRateProvider rateSupplier;
        
        @Override
        public int compare(Currency curA, Currency curB) {
            double rateA = this.rateSupplier.getRate(this.baseCur, curA);
            double rateB = this.rateSupplier.getRate(this.baseCur, curB);
            return Double.compare(rateA, rateB);
        }

        public DraftComparator(Currency base, 
                ExchangeRateProvider rateProvider) {
            this.baseCur = base;
            this.rateSupplier = rateProvider;
        }
        
    }
    
    private static class MockRateProvider implements ExchangeRateProvider {
        
        @Override
        public double getRate(Currency source, Currency target) {
            return source.getDisplayName().compareTo(target.getDisplayName());
        }
        
    }
    
}
