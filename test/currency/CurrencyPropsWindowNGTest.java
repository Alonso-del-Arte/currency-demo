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

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyPropsWindow class.
 * @author Alonso del Arte
 */
public class CurrencyPropsWindowNGTest {
    
    /**
     * Test of itemStateChanged method, of class CurrencyPropsWindow.
     */
    @Test
    public void testItemStateChanged() {
        System.out.println("itemStateChanged");
        ItemEvent ie = null;
        CurrencyPropsWindow instance = new CurrencyPropsWindow();
        instance.itemStateChanged(ie);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of actionPerformed method, of class CurrencyPropsWindow.
     */
    @Test
    public void testActionPerformed() {
        System.out.println("actionPerformed");
        ActionEvent ae = null;
        CurrencyPropsWindow instance = new CurrencyPropsWindow();
        instance.actionPerformed(ae);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}