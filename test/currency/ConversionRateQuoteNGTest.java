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

import static currency.CurrencyChooser.RANDOM;

import java.time.LocalDateTime;
import java.util.Currency;

import static org.testframe.api.Asserters.assertThrows;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the ConversionRateQuote class.
 * @author Alonso del Arte
 */
public class ConversionRateQuoteNGTest {
    
    @Test
    public void testConstructorRejectsNullCurrencyPair() {
        double rate = RANDOM.nextDouble();
        LocalDateTime date = LocalDateTime.now();
        String msg = "Using null currency pair should've caused NPE";
        Throwable t = assertThrows(() -> {
            ConversionRateQuote instance = new ConversionRateQuote(null, rate, 
                    date);
            System.out.println(msg + ", not created instance " 
                    + instance.getClass().getName() + '@' 
                    + Integer.toHexString(System.identityHashCode(instance)));
        }, NullPointerException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be null";
    }
    
}
