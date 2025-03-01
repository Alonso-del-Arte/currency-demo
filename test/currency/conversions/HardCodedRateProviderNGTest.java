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
    
    @Test
    public void testDateOfHardCodingConstant() {
        LocalDate expected = LocalDate.of(2025, Month.FEBRUARY, 28);
        LocalDate actual = HardCodedRateProvider.DATE_OF_HARD_CODING;
        assertEquals(actual, expected);
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
        assertInRange(minimum, actual, maximum, msg);
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
        assertInRange(minimum, actual, maximum, msg);
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
        assertInRange(minimum, actual, maximum, msg);
    }
    
}
