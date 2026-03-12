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
package currency.conversions;

import currency.CurrencyChooser;
import currency.CurrencyPair;
import static currency.conversions.ExchangeRateProviderNGTest.RANDOM;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import static org.testframe.api.Asserters.assertThrows;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the WeightedExchangeRateProvider class.
 * @author Alonso del Arte
 */
public class WeightedExchangeRateProviderNGTest {
    
    private static final Map<Currency, Double> EMPTY_WEIGHT_MAP 
            = new HashMap<>();
    
    private static final ExchangeRateProvider DEFAULT_PROVIDER 
            = new HardCodedRateProvider();
    
    private static Map<Currency, Double> makeWeightsMap() {
        int initialCapacity = RANDOM.nextInt(4, 16);
        Map<Currency, Double> map = new HashMap<>(initialCapacity);
        while (map.size() < initialCapacity) {
            Currency key = CurrencyChooser.chooseCurrency();
            double value = 0.5 + RANDOM.nextDouble();
            map.put(key, value);
        }
        return map;
    }
    
    @Test
    public void testGetWeights() {
        System.out.println("getWeights");
        Map<Currency, Double> expected = makeWeightsMap();
        WeightedExchangeRateProvider instance 
                = new WeightedExchangeRateProvider(expected, DEFAULT_PROVIDER);
        Map<Currency, Double> actual = instance.getWeights();
        assertEquals(actual, expected);
    }
    
    /**
     * Test of getRate method, of class WeightedExchangeRateProvider.
     */
    @Test(enabled = false)
    public void testGetRate() {
        System.out.println("getRate");
//        Currency source = null;
//        Currency target = null;
//        WeightedExchangeRateProvider instance = new WeightedExchangeRateProvider();
//        double expResult = 0.0;
//        double result = instance.getRate(source, target);
//        assertEquals(result, expResult, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRate method, of class WeightedExchangeRateProvider.
     */
    @Test(enabled = false)
    public void testGetRateForCurrencyPair() {
//        CurrencyPair currencies = null;
//        WeightedExchangeRateProvider instance = new WeightedExchangeRateProvider();
//        double expResult = 0.0;
//        double result = instance.getRate(currencies);
//        assertEquals(result, expResult, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    @Test
    public void testConstructorRejectsNullMap() {
        String msg = "Null weights map should cause exception";
        Throwable t = assertThrows(() -> {
            ExchangeRateProvider badInstance 
                    = new WeightedExchangeRateProvider(null, DEFAULT_PROVIDER);
            System.out.println(msg + ", not given " + badInstance.toString());
        }, NullPointerException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
    
    @Test
    public void testConstructorRejectsNullRateProvider() {
        String msg = "Null rate provider should cause exception";
        Throwable t = assertThrows(() -> {
            ExchangeRateProvider badInstance 
                    = new WeightedExchangeRateProvider(EMPTY_WEIGHT_MAP, null);
            System.out.println(msg + ", not given " + badInstance.toString());
        }, NullPointerException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
    
}
