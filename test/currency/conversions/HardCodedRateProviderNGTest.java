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

import java.time.LocalDate;
import java.time.Month;
import java.util.Currency;
import java.util.HashSet;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.testframe.api.Asserters.assertInRange;
import static org.testframe.api.Asserters.assertContainsSame;
import static org.testframe.api.Asserters.assertDoesNotThrow;
import static org.testframe.api.Asserters.assertThrows;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the HardCodedRateProvider class.
 * @author Alonso del Arte
 */
public class HardCodedRateProviderNGTest {
    
    private static final double DEFAULT_VARIANCE = 0.00001;
    
    private static final Currency UNITED_STATES_DOLLARS 
            = Currency.getInstance(Locale.US);
    
    private static final String USD_DISPLAY_NAME 
            = UNITED_STATES_DOLLARS.getDisplayName();
    
    private static final String USD_3_LETTER_CODE 
            = UNITED_STATES_DOLLARS.getCurrencyCode();
    
    private static final String[] SELECTED_NON_USD_CURRENCY_CODES = {"AUD", 
        "BRL", "CAD", "CNY", "EUR", "GBP", "HKD", "ILS", "INR", "IRR", "JPY", 
        "KRW", "KWD", "LBP", "MXN", "NZD", "PHP", "TWD", "VND", "XAF", "XCD", 
        "XOF", "XPF"};
    
    private static final Set<Currency> SELECTED_NON_USD_CURRENCIES 
            = Set.of(SELECTED_NON_USD_CURRENCY_CODES).stream()
                    .map(currencyCode -> Currency.getInstance(currencyCode))
                    .collect(Collectors.toSet());
    
    private static final Set<Currency> ALL_CURRENCIES 
            = Currency.getAvailableCurrencies();
    
    private static final Set<Currency> UNSUPPORTED_CURRENCIES 
            = new HashSet<>(ALL_CURRENCIES);
    
    static {
        UNSUPPORTED_CURRENCIES.remove(UNITED_STATES_DOLLARS);
        UNSUPPORTED_CURRENCIES.removeAll(SELECTED_NON_USD_CURRENCIES);
    }
    
    @Test
    public void testDateOfHardCodingConstant() {
        LocalDate expected = LocalDate.of(2026, Month.JANUARY, 3);
        LocalDate actual = HardCodedRateProvider.DATE_OF_HARD_CODING;
        assertEquals(actual, expected);
    }
    
    @Test
    public void testSupportedCurrencies() {
        SpecificCurrenciesSupport instance = new HardCodedRateProvider();
        Set<Currency> expected = new HashSet<>(ALL_CURRENCIES);
        expected.removeAll(UNSUPPORTED_CURRENCIES);
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
        double minimum = 1.29;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, austrDollar);
        double maximum = 1.59;
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
        double maximum = 6.19;
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
        double maximum = 1.45;
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
        double minimum = 6.32;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, chineseYuan);
        double maximum = 7.32;
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
        double minimum = 0.82;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, euro);
        double maximum = 1.03;
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
        double minimum = 3.09;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, israeliShekel);
        double maximum = 4.05;
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
        double minimum = 72.44;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, indianRupee);
        double maximum = 90.95;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + indianRupee.getDisplayName() 
                + " (" + indianRupee.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToIRR() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency iranianRial = Currency.getInstance("IRR");
        double minimum = 42000.0;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, iranianRial);
        double maximum = 1500000.0;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + iranianRial.getDisplayName() 
                + " (" + iranianRial.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToJPY() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency japaneseYen = Currency.getInstance("JPY");
        double minimum = 103.93;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, japaneseYen);
        double maximum = 160.88;
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
        double minimum = 1092.82;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, korWon);
        double maximum = 1478.09;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + korWon.getDisplayName() + " (" 
                + korWon.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToKWD() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency kuwaitiDinar = Currency.getInstance("KWD");
        double minimum = 0.25;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, kuwaitiDinar);
        double maximum = 0.35;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + kuwaitiDinar.getDisplayName() + " (" 
                + kuwaitiDinar.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRateUSDToMXN() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency mexPeso = Currency.getInstance("MXN");
        double minimum = 16.55;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, mexPeso);
        double maximum = 21.89;
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
        double minimum = 1.34;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, 
                newZealandDollar);
        double maximum = 1.78;
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
        double minimum = 47.66;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, philPeso);
        double maximum = 58.95;
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
        double minimum = 27.64;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, taiwanDollar);
        double maximum = 32.93;
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
        double minimum = 22394.0;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, vietDong);
        double maximum = 26375.0;
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
        double minimum = 539.29;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, cfaFranc);
        double maximum = 674.12;
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
        double minimum = 534.0;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, 
                westAfricanFranc);
        double maximum = 676.98;
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
        double minimum = 101.14;
        double actual = instance.getRate(UNITED_STATES_DOLLARS, cfpFranc);
        double maximum = 102.60;
        String msg = "Rate of conversion from " + USD_DISPLAY_NAME + " (" 
                + USD_3_LETTER_CODE + ") to " + cfpFranc.getDisplayName() 
                + " (" + cfpFranc.getCurrencyCode() 
                + ") should be in the range of the past 5 years";
        assertInRange(minimum, actual, maximum, DEFAULT_VARIANCE, msg);
    }
    
    @Test
    public void testGetRate() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        double delta = 0.01;
        for (Currency currency : SELECTED_NON_USD_CURRENCIES) {
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
    
    @Test
    public void testGetRateNeitherCurrencyUSD() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency source = CurrencyChooser.chooseCurrency(SELECTED_NON_USD_CURRENCIES);
        String msgPart = "Inquiring rate for " + source.getDisplayName() + " (" 
                + source.getCurrencyCode() + ") to ";
        for (Currency target : SELECTED_NON_USD_CURRENCIES) {
            double expected = instance.getRate(UNITED_STATES_DOLLARS, target) 
                    / instance.getRate(UNITED_STATES_DOLLARS, source);
            double actual = instance.getRate(source, target);
            String message = msgPart + target.getDisplayName() + " (" 
                    + target.getCurrencyCode() + ")";
            assertEquals(actual, expected, DEFAULT_VARIANCE, message);
        }
    }

    @Test
    public void testUnsupportedSourceCurrencyCausesException() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency from = CurrencyChooser.chooseCurrency(UNSUPPORTED_CURRENCIES);
        Currency to = CurrencyChooser.chooseCurrency(SELECTED_NON_USD_CURRENCIES);
        CurrencyPair currencies = new CurrencyPair(from, to);
        String fromCurrCode = from.getCurrencyCode();
        String msg = "Since " + from.getDisplayName() + " (" + fromCurrCode 
                + ") is not supported, conversion from " + fromCurrCode + " to " 
                + to.getDisplayName() + " (" + to.getCurrencyCode() 
                + ") should cause exception";
        Throwable t = assertThrows(() -> {
            double badRate = instance.getRate(currencies);
            System.out.println(msg + ", not given result " + badRate);
        }, NoSuchElementException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        String containsMsg = "Exception message should contain currency code " 
                + fromCurrCode;
        assert excMsg.contains(fromCurrCode) : containsMsg;
    }

    @Test
    public void testUnsupportedSourceCurrencyTwoParamGetRateCausesException() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency source 
                 = CurrencyChooser.chooseCurrency(UNSUPPORTED_CURRENCIES);
        Currency target = CurrencyChooser.chooseCurrency(SELECTED_NON_USD_CURRENCIES);
        String fromCurrCode = source.getCurrencyCode();
        String msg = "Since " + source.getDisplayName() + " (" + fromCurrCode 
                + ") is not supported, conversion from " + fromCurrCode + " to " 
                + target.getDisplayName() + " (" + target.getCurrencyCode() 
                + ") should cause exception";
        Throwable t = assertThrows(() -> {
            double badRate = instance.getRate(source, target);
            System.out.println(msg + ", not given result " + badRate);
        }, NoSuchElementException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        String containsMsg = "Exception message should contain currency code " 
                + fromCurrCode;
        assert excMsg.contains(fromCurrCode) : containsMsg;
    }

    @Test
    public void testUnsupportedTargetCurrencyCausesException() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency from = CurrencyChooser.chooseCurrency(SELECTED_NON_USD_CURRENCIES);
        Currency to = CurrencyChooser.chooseCurrency(UNSUPPORTED_CURRENCIES);
        CurrencyPair currencies = new CurrencyPair(from, to);
        String toCurrCode = to.getCurrencyCode();
        String msg = "Since " + to.getDisplayName() + " (" + toCurrCode 
                + ") is not supported, conversion from " 
                + from.getDisplayName() + " (" + from.getCurrencyCode() 
                + ") to " + toCurrCode + " should cause exception";
        Throwable t = assertThrows(() -> {
            double badRate = instance.getRate(currencies);
            System.out.println(msg + ", not given result " + badRate);
        }, NoSuchElementException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        String containsMsg = "Exception message should contain currency code " 
                + toCurrCode;
        assert excMsg.contains(toCurrCode) : containsMsg;
    }

    @Test
    public void testUnsupportedTargetCurrencyTwoParamGetRateCausesException() {
        ExchangeRateProvider instance = new HardCodedRateProvider();
        Currency source = CurrencyChooser.chooseCurrency(SELECTED_NON_USD_CURRENCIES);
        Currency target 
                = CurrencyChooser.chooseCurrency(UNSUPPORTED_CURRENCIES);
        String toCurrCode = target.getCurrencyCode();
        String msg = "Since " + target.getDisplayName() + " (" + toCurrCode 
                + ") is not supported, conversion from " 
                + source.getDisplayName() + " (" + source.getCurrencyCode() 
                + ") to " + toCurrCode + " should cause exception";
        Throwable t = assertThrows(() -> {
            double badRate = instance.getRate(source, target);
            System.out.println(msg + ", not given result " + badRate);
        }, NoSuchElementException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        String containsMsg = "Exception message should contain currency code " 
                + toCurrCode;
        assert excMsg.contains(toCurrCode) : containsMsg;
    }

}
