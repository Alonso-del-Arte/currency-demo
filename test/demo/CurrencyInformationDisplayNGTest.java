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
import currency.comparators.LetterCodeComparator;

import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Currency;

import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.WindowConstants;

import static org.testframe.api.Asserters.assertThrows;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyInformationDisplay class.
 * @author Alonso del Arte
 */
public class CurrencyInformationDisplayNGTest implements ItemListener {
    
    private static final String EXPECTED_PARTIAL_TITLE 
            = "Currency Information for ";
    
    private boolean haveReceivedItemChangeEvent = false;
    
    // TODO: Change source above source 13, use switch expression
    private static String itemStateLabel(int stateCode) {
        switch (stateCode) {
            case ItemEvent.DESELECTED:
                return "deselected";
            case ItemEvent.ITEM_FIRST:
                return "item first";
            case ItemEvent.SELECTED:
                return "selected";
            default:
                return "not recognized";
        }
    }
    
    @Override
    public void itemStateChanged(ItemEvent ie) {
        this.haveReceivedItemChangeEvent = true;
        System.out.println("Affected item is " + ie.getItem().toString());
        String itemStateStr = itemStateLabel(ie.getStateChange());
        System.out.println("Item state is " + itemStateStr);
    }

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
    
    @Test
    public void testDisplayNameFieldHasSpecifiedNumberOfColumns() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        int expected = CurrencyInformationDisplay.DEFAULT_TEXT_FIELD_COLUMNS;
        int actual = instance.displayNameField.getColumns();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testLetterCodeFieldNotEditable() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        String msg = "Letter code field should not be editable to end user";
        assert !instance.letterCodeField.isEditable() : msg;
    }
    
    @Test
    public void testNumberCodeFieldNotEditable() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        String msg = "Number code field should not be editable to end user";
        assert !instance.numberCodeField.isEditable() : msg;
    }
    
    @Test
    public void testSymbolFieldNotEditable() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        String msg = "Symbol field should not be editable to end user";
        assert !instance.symbolField.isEditable() : msg;
    }
    
    @Test
    public void testFractionDigitsFieldNotEditable() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        String msg = "Fraction digits field should not be editable to end user";
        assert !instance.fractionDigitsField.isEditable() : msg;
    }

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
    
    @Test
    public void testDisplayNameSetToCurrencySpecifiedInConstructor() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        String expected = currency.getDisplayName();
        String actual = instance.displayNameField.getText();
        String message = "Display should show display name for currency " 
                + currency.getCurrencyCode();
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testLetterCodePerCurrencySpecifiedInConstructor() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        String expected = currency.getCurrencyCode();
        String actual = instance.letterCodeField.getText();
        String message = "Display should show letter code for currency " 
                + currency.getDisplayName();
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testNumericCodePerCurrencySpecifiedInConstructor() {
        Currency currency = CurrencyChooser.chooseCurrency(
                (curr) -> curr.getNumericCode() > 99
        );
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        String expected = Integer.toString(currency.getNumericCode());
        String actual = instance.numberCodeField.getText();
        String message = "Display should show number code for currency " 
                + currency.getDisplayName() + " (" + currency.getCurrencyCode() 
                + ")";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testSingleZeroPaddedNumericCodePerCurrencyInConstructor() {
        Currency currency = CurrencyChooser.chooseCurrency(
                (curr) -> Math.floor(Math.log10(curr.getNumericCode())) == 1.0
        );
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        String expected = "0" + Integer.toString(currency.getNumericCode());
        String actual = instance.numberCodeField.getText();
        String message = "Display should show zero-padded number code for " 
                + currency.getDisplayName() + " (" + currency.getCurrencyCode() 
                + ")";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testZeroPaddedNumericCodePerCurrencyInConstructor() {
        Currency currency = CurrencyChooser.chooseCurrency(
                (curr) -> curr.getNumericCode() < 10
        );
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        String expected = "00" + Integer.toString(currency.getNumericCode());
        String actual = instance.numberCodeField.getText();
        String message = "Display should show zero-padded number code for " 
                + currency.getDisplayName() + " (" + currency.getCurrencyCode() 
                + ")";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testSymbolPerCurrencySpecifiedInConstructor() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        String expected = currency.getSymbol();
        String actual = instance.symbolField.getText();
        String message = "Display should show symbol " + expected 
                + " for currency " + currency.getDisplayName();
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testFractionDigitsPerCurrencySpecifiedInConstructor() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        String expected = Integer.toString(currency.getDefaultFractionDigits());
        String actual = instance.fractionDigitsField.getText();
        String message = "Display should show default fraction digits " 
                + expected + " for currency " + currency.getDisplayName() + " (" 
                + currency.getCurrencyCode() + ")";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testComboBoxHasConstructorSpecifiedCurrencySelectedAtFirst() {
        Currency expected = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(expected);
        Currency actual = (Currency) instance.currenciesDropdown
                .getSelectedItem();
        String message = "Constructor specified " + expected.getDisplayName() 
                + " (" + expected.getCurrencyCode() + "), dropdown shows " 
                + actual.getDisplayName() + " (" + actual.getCurrencyCode() 
                + ")";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testComboBoxListsCurrenciesAlphabeticallyByLetterCode() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        Currency[] expected = CurrencyChooser.getSuitableCurrencies()
                .toArray(Currency[]::new);
        Arrays.sort(expected, new LetterCodeComparator());
        int size = instance.currenciesDropdown.getItemCount();
        Currency[] actual = new Currency[size];
        for (int index = 0; index < size; index++) {
            actual[index] = instance.currenciesDropdown.getItemAt(index);
        }
        String message = "Currencies should be sorted by letter code";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testDisplayReflectsComboBoxSelectionInTitle() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        instance.activate();
        Currency secondCurrency = CurrencyChooser
                .chooseCurrencyOtherThan(currency);
        instance.currenciesDropdown.addItemListener(this);
        instance.currenciesDropdown.setSelectedItem(secondCurrency);
        String expected = EXPECTED_PARTIAL_TITLE 
                + secondCurrency.getCurrencyCode();
        String actual = instance.getTitle();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testDisplayReflectsComboBoxSelectionInDisplayName() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        instance.activate();
        Currency secondCurrency = CurrencyChooser
                .chooseCurrencyOtherThan(currency);
        instance.currenciesDropdown.addItemListener(this);
        instance.currenciesDropdown.setSelectedItem(secondCurrency);
        String expected = secondCurrency.getDisplayName();
        String actual = instance.displayNameField.getText();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testDisplayReflectsComboBoxSelectionIn3LetterCode() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        instance.activate();
        Currency secondCurrency = CurrencyChooser
                .chooseCurrencyOtherThan(currency);
        instance.currenciesDropdown.addItemListener(this);
        instance.currenciesDropdown.setSelectedItem(secondCurrency);
        String expected = secondCurrency.getCurrencyCode();
        String actual = instance.letterCodeField.getText();
        String message = "Expecting \"" + expected + "\" for " 
                + secondCurrency.getDisplayName();
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testDisplayReflectsComboBoxSelectionIn3DigitCode() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        instance.activate();
        Currency secondCurrency = CurrencyChooser.chooseCurrency(
                (curr) -> curr.getNumericCode() > 99 && curr != currency
        );
        instance.currenciesDropdown.addItemListener(this);
        instance.currenciesDropdown.setSelectedItem(secondCurrency);
        String expected = Integer.toString(secondCurrency.getNumericCode());
        String actual = instance.numberCodeField.getText();
        String message = "Expecting \"" + expected + "\" for " 
                + secondCurrency.getDisplayName() + " (" 
                + secondCurrency.getCurrencyCode() + ")";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testDisplayReflectsComboBoxSelSingleZeroPadded3DigitCode() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        instance.activate();
        Currency secondCurrency = CurrencyChooser.chooseCurrency(
                (curr) -> Math.floor(Math.log10(curr.getNumericCode())) == 1.0
                        && curr != currency
        );
        instance.currenciesDropdown.addItemListener(this);
        instance.currenciesDropdown.setSelectedItem(secondCurrency);
        String expected = "0" + Integer.toString(secondCurrency
                .getNumericCode());
        String actual = instance.numberCodeField.getText();
        String message = "Expecting \"" + expected + "\" for " 
                + secondCurrency.getDisplayName() + " (" 
                + secondCurrency.getCurrencyCode() + ")";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testDisplayReflectsComboBoxSelDoubleZeroPadded3DigitCode() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        instance.activate();
        Currency secondCurrency = CurrencyChooser.chooseCurrency(
                (curr) -> curr.getNumericCode() < 10 && curr != currency
        );
        instance.currenciesDropdown.addItemListener(this);
        instance.currenciesDropdown.setSelectedItem(secondCurrency);
        String expected = "00" + Integer.toString(secondCurrency
                .getNumericCode());
        String actual = instance.numberCodeField.getText();
        String message = "Expecting \"" + expected + "\" for " 
                + secondCurrency.getDisplayName() + " (" 
                + secondCurrency.getCurrencyCode() + ")";
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testDisplayReflectsComboBoxSelectionInSymbol() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        instance.activate();
        Currency secondCurrency = CurrencyChooser
                .chooseCurrencyOtherThan(currency);
        instance.currenciesDropdown.addItemListener(this);
        instance.currenciesDropdown.setSelectedItem(secondCurrency);
        String expected = secondCurrency.getSymbol();
        String actual = instance.symbolField.getText();
        String message = "Expecting \"" + expected + "\" for " 
                + secondCurrency.getDisplayName();
        assertEquals(actual, expected, message);
    }
    
    private static boolean contains(Component[] components, 
            Component component) {
        boolean found = false;
        int len = components.length;
        int index = 0;
        while (!found && index < len) {
            found = components[index].equals(component);
            index++;
        }
        return found;
    }
    
    @Test(enabled = false)
    public void testDisplayIncludesJPanel() {
        Currency currency = CurrencyChooser.chooseCurrency();
        CurrencyInformationDisplay instance 
                = new CurrencyInformationDisplay(currency);
        instance.activate();
        JPanel panel = null;
        boolean jPanelFound = false;
        JRootPane rootPane = instance.getRootPane();
        Component[] components = rootPane.getComponents();
        for (Component component : components) {
            System.out.println(component.toString());
            if (component instanceof JPanel) {
                panel = (JPanel) component;
                jPanelFound = true;
            }
        }
        String msg = "Display should include JPanel";
        assert jPanelFound : msg;
        assert panel != null : msg;
        Component[] panelComponents = rootPane.getComponents();
        System.out.println(java.util.Arrays.toString(panelComponents));
        assert contains(panelComponents, instance.displayNameField) 
                : "Components should include display name field";
        assert contains(panelComponents, instance.letterCodeField) 
                : "Components should include letter code field";
        assert contains(panelComponents, instance.numberCodeField) 
                : "Components should include number code field";
        assert contains(panelComponents, instance.symbolField) 
                : "Components should include symbol field";
        assert contains(panelComponents, instance.fractionDigitsField) 
                : "Components should include fraction digits field";
    }

}
