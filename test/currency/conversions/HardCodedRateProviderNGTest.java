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
package currency.conversions;

import currency.CurrencyChooser;
import currency.CurrencyPair;
import currency.SpecificCurrenciesSupport;

import java.time.LocalDate;
import java.time.Month;
import java.util.Currency;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static org.testframe.api.Asserters.assertInRange;
import static org.testframe.api.Asserters.assertContainsSame;
import static org.testframe.api.Asserters.assertDoesNotThrow;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the HardCodedRateProvider class.
 * @author Alonso del Arte
 */
public class HardCodedRateProviderNGTest {
    
    private static final Currency UNITED_STATES_DOLLARS 
            = Currency.getInstance(Locale.US);
    
    private static final String USD_DISPLAY_NAME 
            = UNITED_STATES_DOLLARS.getDisplayName();
    
    private static final String USD_3_LETTER_CODE 
            = UNITED_STATES_DOLLARS.getCurrencyCode();
    
    private static final double DEFAULT_VARIANCE = 0.00001;
    
    @Test
    public void testDateOfHardCodingConstant() {
        LocalDate expected = LocalDate.of(2025, Month.MARCH, 3);
        LocalDate actual = HardCodedRateProvider.DATE_OF_HARD_CODING;
        assertEquals(actual, expected);
    }
    
    @Test
    public void testSupportedCurrencies() {
        SpecificCurrenciesSupport instance = new HardCodedRateProvider();
        String[] currencyCodes = {"AUD", "BRL", "CAD", "CNY", "EUR", "GBP", 
            "HKD", "ILS", "INR", "JPY", "KRW", "MXN", "NZD", "PHP", "TWD", 
            "USD", "VND", "XAF", "XCD", "XOF", "XPF"};
        Set<Currency> expected = Set.of(currencyCodes).stream()
                .map(currencyCode -> Currency.getInstance(currencyCode))
                .collect(Collectors.toSet());
        Set<Currency> actual = instance.supportedCurrencies();
        assertContainsSame(expected, actual);
    }
    
    @Test
    public void testSupportedCurrenciesDoesNotLeakField() {
        SpecificCurrenciesSupport instance = new HardCodedRateProvider();
        Set<Currency> initial = instance.supportedCurrencies();
        Currency currency = CurrencyChooser.chooseCurrency(
                cur -> !initial.contains(cur)
        );
        String msg = "Trying to add " + currency.getDisplayName() + " (" 
                + currency.getCurrencyCode() 
                + ") to reported set should not leak field nor cause exception";
        assertDoesNotThrow(() -> {
            Set<Currency> expected = new HashSet<>(initial);
            initial.add(currency);
            Set<Currency> actual = instance.supportedCurrencies();
            assertContainsSame(expected, actual, msg);
        }, msg);
    }
    
    @Test
    public void testGetRateUSDToAUD() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency austrDollar = Currency.getInstance("AUD");
        double minimum = 1.44533;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, austrDollar);
        double maximum = 1.64034;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + austrDollar.getDisplayName() + " (" 
                + austrDollar.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToBRL() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency brazReal = Currency.getInstance("BRL");
        double minimum = 4.47;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, brazReal);
        double maximum = 6.29;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + brazReal.getDisplayName() + " (" 
                + brazReal.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToCAD() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency canadianDollar = Currency.getInstance("CAD");
        double minimum = 1.2;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, canadianDollar);
        double maximum = 1.5;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + canadianDollar.getDisplayName() 
                + " (" + canadianDollar.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToCNY() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency chineseYuan = Currency.getInstance("CNY");
        double minimum = 6.0;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, chineseYuan);
        double maximum = 7.5;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + chineseYuan.getDisplayName() 
                + " (" + chineseYuan.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToEUR() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency euro = Currency.getInstance("EUR");
        double minimum = 0.892811;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, euro);
        double maximum = 0.978298;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + euro.getDisplayName() + " (" 
                + euro.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToGBP() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency britPound = Currency.getInstance(Locale.UK);
        double minimum = 0.7;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, britPound);
        double maximum = 0.93;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + britPound.getDisplayName() + " (" 
                + britPound.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToHKD() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency hongKongDollar = Currency.getInstance("HKD");
        double minimum = 7.7;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, hongKongDollar);
        double maximum = 7.85;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + hongKongDollar.getDisplayName() + " (" 
                + hongKongDollar.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToILS() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency israeliShekel = Currency.getInstance("ILS");
        double minimum = 3.0;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, israeliShekel);
        double maximum = 4.5;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + israeliShekel.getDisplayName() 
                + " (" + israeliShekel.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToINR() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency indianRupee = Currency.getInstance("INR");
        double minimum = 70.0;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, indianRupee);
        double maximum = 90.0;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + indianRupee.getDisplayName() 
                + " (" + indianRupee.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToJPY() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency japaneseYen = Currency.getInstance("JPY");
        double minimum = 100.0;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, japaneseYen);
        double maximum = 160.0;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + japaneseYen.getDisplayName() 
                + " (" + japaneseYen.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToKRW() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency korWon = Currency.getInstance(Locale.KOREA);
        double minimum = 1083.85;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, korWon);
        double maximum = 1478.09;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + korWon.getDisplayName() + " (" 
                + korWon.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToMXN() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency mexPeso = Currency.getInstance("MXN");
        double minimum = 15.0;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, mexPeso);
        double maximum = 25.0;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + mexPeso.getDisplayName() + " (" 
                + mexPeso.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToNZD() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency newZealandDollar = Currency.getInstance("NZD");
        double minimum = 1.2;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, 
                newZealandDollar);
        double maximum = 1.8;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " 
                + newZealandDollar.getDisplayName() + " (" 
                + newZealandDollar.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToPHP() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency philPeso = Currency.getInstance("PHP");
        double minimum = 45.0;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, philPeso);
        double maximum = 60.0;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + philPeso.getDisplayName() + " (" 
                + philPeso.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToTWD() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency taiwanDollar = Currency.getInstance("TWD");
        double minimum = 32.6;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, taiwanDollar);
        double maximum = 33.0;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + taiwanDollar.getDisplayName() + " (" 
                + taiwanDollar.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToUSD() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        double minimum = 0.9999;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, 
                UNITED_STATES_DOLLARS);
        double maximum = 1.0001;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToVND() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency vietDong = Currency.getInstance("VND");
        double minimum = 22000.0;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, vietDong);
        double maximum = 26000.0;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + vietDong.getDisplayName() + " (" 
                + vietDong.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToXAF() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency cfaFranc = Currency.getInstance("XAF");
        double minimum = 500.0;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, cfaFranc);
        double maximum = 700.0;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + cfaFranc.getDisplayName() + " (" 
                + cfaFranc.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToXCD() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency eastCaribDollar = Currency.getInstance("XCD");
        double minimum = 2.69;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, 
                eastCaribDollar);
        double maximum = 2.71;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + eastCaribDollar.getDisplayName() 
                + " (" + eastCaribDollar.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToXOF() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency westAfricanFranc = Currency.getInstance("XOF");
        double minimum = 500.0;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, 
                westAfricanFranc);
        double maximum = 700.0;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + westAfricanFranc.getDisplayName() 
                + " (" + westAfricanFranc.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToXPF() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency cfpFranc = Currency.getInstance("XPF");
        double minimum = 113.0;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, cfpFranc);
        double maximum = 116.0;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + cfpFranc.getDisplayName() 
                + " (" + cfpFranc.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRate() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        String[] nonUSDCurrencyCodes = {"AUD", "BRL", "CAD", "CNY", "EUR", "GBP", 
            "HKD", "ILS", "INR", "JPY", "KRW", "MXN", "NZD", "PHP", "TWD", 
            "VND", "XAF", "XCD", "XOF", "XPF"};
        Set<Currency> nonUSDCurrencies = Set.of(nonUSDCurrencyCodes)
                .stream().map(currencyCode 
                        -> Currency.getInstance(currencyCode))
                .collect(Collectors.toSet());
        double delta = 0.01;
        for (Currency currency : nonUSDCurrencies) {
            CurrencyPair pair 
                    = new CurrencyPair(UNITED_STATES_DOLLARS, currency);
            double rate = instance.getRate(pair);
            CurrencyPair currencies = pair.flip();
            double expected = 1.0 / rate;
            double actual = instance.getRate(currencies);
            String message = "As conversion rate for " + USD_DISPLAY_NAME + " (" 
                    + USD_3_LETTER_CODE + ") to " + currency.getDisplayName() 
                    + " (" + currency.getCurrencyCode() + ") is said to be " 
                    + rate + ", conversion for " + currencies.toString() 
                    + " should be " + expected;
            assertEquals(actual, expected, delta, message);
        }
    }
    
    // TODO: Write tests for neither source nor target USD
    
}
