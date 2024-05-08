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

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.Currency;
import java.util.Locale;

import static org.testframe.api.Asserters.assertThrows;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyPropsWindow class.
 * @author Alonso del Arte
 */
public class CurrencyPropsWindowNGTest {
    
    @Test
    public void testNoSecondActivation() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyPropsWindow window = new CurrencyPropsWindow(currency);
        window.activate();
        System.out.println("Activated window the first time");
        String msg = "Should not have been able to activate window twice";
        Throwable t = assertThrows(() -> {
            window.activate();
            System.out.println("Activated window a second time");
        }, IllegalStateException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
       
}
