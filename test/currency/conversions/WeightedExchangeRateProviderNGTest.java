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
import currency.SpecificCurrenciesSupport;
import static currency.conversions.ExchangeRateProviderNGTest.RANDOM;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    
    private static final Set<Currency> AVAILABLE_CURRENCIES 
            = ((SpecificCurrenciesSupport) DEFAULT_PROVIDER)
                    .supportedCurrencies();
    
    private static final double DEFAULT_DELTA = 0.0001;
    
    private static Map<Currency, Double> makeWeightsMap() {
        int initialCapacity = RANDOM.nextInt(4, 16);
        Map<Currency, Double> map = new HashMap<>(initialCapacity);
        while (map.size() < initialCapacity) {
            Currency key = CurrencyChooser.chooseCurrency(AVAILABLE_CURRENCIES);
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
    
    @Test
    public void testGetWeightsDoesNotLeakField() {
        Map<Currency, Double> weights = makeWeightsMap();
        WeightedExchangeRateProvider instance 
                = new WeightedExchangeRateProvider(weights, DEFAULT_PROVIDER);
        Map<Currency, Double> providedWeights = instance.getWeights();
        Map<Currency, Double> expected = new HashMap<>(providedWeights);
        Set<Currency> keys = providedWeights.keySet();
        for (Currency key : keys) {
            double wrongValue = RANDOM.nextInt(-20, 1);
            providedWeights.put(key, wrongValue);
        }
        Map<Currency, Double> actual = instance.getWeights();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testGetRateRejectsNullSource() {
        ExchangeRateProvider instance 
                = new WeightedExchangeRateProvider(EMPTY_WEIGHT_MAP, 
                        DEFAULT_PROVIDER);
        Currency source = null;
        Currency target = CurrencyChooser.chooseCurrency(AVAILABLE_CURRENCIES);
        String msg = "Null source and target " + target.getDisplayName() + " (" 
                + target.getCurrencyCode() + ") should cause an exception";
        Throwable t = assertThrows(() -> {
            double badResult = instance.getRate(source, target);
            System.out.println(msg + ", not given result " + badResult);
        }, NullPointerException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
    
    @Test
    public void testGetRateRejectsNullTarget() {
        ExchangeRateProvider instance 
                = new WeightedExchangeRateProvider(EMPTY_WEIGHT_MAP, 
                        DEFAULT_PROVIDER);
        Currency source = CurrencyChooser.chooseCurrency(AVAILABLE_CURRENCIES);
        Currency target = null;
        String msg = "Source " + source.getDisplayName() + " (" 
                + source.getCurrencyCode() 
                + ") and null target should cause an exception";
        Throwable t = assertThrows(() -> {
            double badResult = instance.getRate(source, target);
            System.out.println(msg + ", not given result " + badResult);
        }, NullPointerException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
    
    @Test
    public void testGetRateRejectsNullCurrencies() {
        ExchangeRateProvider instance 
                = new WeightedExchangeRateProvider(EMPTY_WEIGHT_MAP, 
                        DEFAULT_PROVIDER);
        String msg = "Null currency pair should cause an exception";
        Throwable t = assertThrows(() -> {
            double badResult = instance.getRate(null);
            System.out.println(msg + ", not given result " + badResult);
        }, NullPointerException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
    
    @Test
    public void testGetRateUnweighted() {
        ExchangeRateProvider instance 
                = new WeightedExchangeRateProvider(EMPTY_WEIGHT_MAP, 
                        DEFAULT_PROVIDER);
        Currency source = CurrencyChooser.chooseCurrency(AVAILABLE_CURRENCIES);
        Currency target = CurrencyChooser.chooseCurrencyOtherThan(source, 
                AVAILABLE_CURRENCIES);
        double expected = DEFAULT_PROVIDER.getRate(source, target);
        double actual = instance.getRate(source, target);
        String message = "Getting rate for " + source.getDisplayName() + " (" 
                + source.getCurrencyCode() + ") to " + target.getDisplayName() 
                + " (" + target.getCurrencyCode() + ")";
        assertEquals(actual, expected, DEFAULT_DELTA, message);
    }
    
    @Test
    public void testGetRateUnweightedFromCurrencyPair() {
        ExchangeRateProvider instance 
                = new WeightedExchangeRateProvider(EMPTY_WEIGHT_MAP, 
                        DEFAULT_PROVIDER);
        Currency from = CurrencyChooser.chooseCurrency(AVAILABLE_CURRENCIES);
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from, 
                AVAILABLE_CURRENCIES);
        CurrencyPair currencies = new CurrencyPair(from, to);
        double expected = DEFAULT_PROVIDER.getRate(currencies);
        double actual = instance.getRate(currencies);
        String message = "Getting rate for " + from.getDisplayName() + " (" 
                + from.getCurrencyCode() + ") to " + to.getDisplayName() 
                + " (" + to.getCurrencyCode() + ")";
        assertEquals(actual, expected, DEFAULT_DELTA, message);
    }
    
    /**
     * Test of the getRate function, of the WeightedExchangeRateProvider class.
     */
    @Test
    public void testGetRate() {
        System.out.println("getRate");
        Map<Currency, Double> weights = makeWeightsMap();
        WeightedExchangeRateProvider instance 
                = new WeightedExchangeRateProvider(weights, DEFAULT_PROVIDER);
        for (Map.Entry<Currency, Double> entry : weights.entrySet()) {
            Currency target = entry.getKey();
            double weight = entry.getValue();
            Currency source = CurrencyChooser.chooseCurrencyOtherThan(target, 
                    AVAILABLE_CURRENCIES);
            double expected = DEFAULT_PROVIDER.getRate(source, target) * weight;
            double actual = instance.getRate(source, target);
            String message = "Considering that " + target.getDisplayName() 
                    + " (" + target.getCurrencyCode() 
                    + ") has been given a test weight of " + weight 
                    + ", getting conversion from " + source.getDisplayName() 
                    + " (" + source.getCurrencyCode() + ") to " 
                    + target.getCurrencyCode();
            assertEquals(actual, expected, DEFAULT_DELTA, message);
        }
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
    
    @Test
    public void testConstructorCopiesWeightsMapForItsOwnUse() {
        Map<Currency, Double> weights = makeWeightsMap();
        Map<Currency, Double> expected = new HashMap<>(weights);
        WeightedExchangeRateProvider instance 
                = new WeightedExchangeRateProvider(weights, DEFAULT_PROVIDER);
        weights.clear();
        Map<Currency, Double> actual = instance.getWeights();
        assertEquals(actual, expected);
    }
    
}
