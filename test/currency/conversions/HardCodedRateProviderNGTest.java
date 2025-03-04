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

import currency.CurrencyPair;

import java.time.LocalDate;
import java.time.Month;
import java.util.Currency;
import java.util.Locale;

import static org.testframe.api.Asserters.assertInRange;

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
    
    // TODO: Write tests for source not USD
    
}
