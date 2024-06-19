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
package demo;

import currency.CurrencyChooser;
import currency.CurrencyConverter;
import currency.MoneyAmount;

import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.util.Currency;

import javax.swing.WindowConstants;

import static org.testframe.api.Asserters.assertThrows;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyInformationDisplay class.
 * @author Alonso del Arte
 */
public class CurrencyInformationDisplayNGTest {
    
    private static final String EXPECTED_PARTIAL_TITLE 
            = "Currency Information for ";
    
    @Test
    public void testNoActivateTwice() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        instance.activate();
        String msg = "Should not have been able to activate " 
                + instance.toString() + " twice";
        Throwable t = assertThrows(() -> {
            instance.activate();
            System.out.println("Activated window a second time");
        }, IllegalStateException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
    
    /**
     * Test of the activate procedure, of the CurrencyInformationDisplay class.
     */
    @Test
    public void testActivate() {
        System.out.println("activate");
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        instance.activate();
        String msg = "Instance " + instance.toString() 
                + " should be visible after activation";
        assert instance.isVisible() : msg;
    }
    
    @Test
    public void testConstructorSetsCurrency() {
        Currency expected = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(expected);
        Currency actual = instance.getCurrency();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testGetSetCurrency() {
        System.out.println("getCurrency");
        System.out.println("setCurrency");
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        Currency expected = CurrencyChooser.chooseCurrencyOtherThan(currency);
        instance.setCurrency(expected);
        Currency actual = instance.getCurrency();
        String message = "Instance initialized with " 
                + currency.getCurrencyCode() + " should be able to change to " 
                + expected.getCurrencyCode();
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testTitleShouldUpdateOnDifferentCurrencyChosen() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        String originalTitle = instance.getTitle();
        Currency otherCurrency 
                = CurrencyChooser.chooseCurrencyOtherThan(currency);
        instance.setCurrency(otherCurrency);
        String expected = EXPECTED_PARTIAL_TITLE + otherCurrency;
        String actual = instance.getTitle();
        String message = "Instance initialized as \"" + originalTitle 
                + "\" should be able to change to \"" + expected + "\"";
        assertEquals(actual, expected, message);
    }
    
    private static String defaultCloseOperationText(int operation) {
        switch (operation) {
            case WindowConstants.DO_NOTHING_ON_CLOSE:
                return "DO_NOTHING_ON_CLOSE";
            case WindowConstants.HIDE_ON_CLOSE:
                return "HIDE_ON_CLOSE";
            case WindowConstants.DISPOSE_ON_CLOSE:
                return "DISPOSE_ON_CLOSE";
            case WindowConstants.EXIT_ON_CLOSE:
                return "EXIT_ON_CLOSE";
            default:
                return "Unrecognized operation";
        }
        // TODO: Figure out how set -source 21 for this project
//        return switch (operation) {
//            case WindowConstants.DO_NOTHING_ON_CLOSE -> "DO_NOTHING_ON_CLOSE";
//            case WindowConstants.HIDE_ON_CLOSE -> "HIDE_ON_CLOSE";
//            case WindowConstants.DISPOSE_ON_CLOSE -> "DISPOSE_ON_CLOSE";
//            case WindowConstants.EXIT_ON_CLOSE -> "EXIT_ON_CLOSE";
//            default -> "Unrecognized operation";
//        };
    }
    
    @Test
    public void testDisplayHasExitOnCloseAsDefaultCloseOperation() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        instance.activate();
        int expected = WindowConstants.EXIT_ON_CLOSE;
        int actual = instance.getDefaultCloseOperation();
        String message = "Default close operation should be EXIT_ON_CLOSE, got " 
                + defaultCloseOperationText(actual);
        assertEquals(actual, expected, message);
    }
    
    // TODO: Test that display uses GridLayout
    
    @Test
    public void testDisplayNameFieldNotEditable() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        String msg = "Display name field should not be editable to end user";
        assert !instance.displayNameField.isEditable() : msg;
    }
    
    // TODO: Test that text fields are not editable

    // TODO: Test that text fields have specific number of columns
    
    // TODO: Test that numeric code field zero pads when necessary
    
    // TODO: Test that item state change causes currency to change
    
    @Test
    public void testConstructorSetsCurrencyInTitle() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        String expected = EXPECTED_PARTIAL_TITLE + currency.getCurrencyCode();
        String actual = instance.getTitle();
        assertEquals(actual, expected);
    }

}
