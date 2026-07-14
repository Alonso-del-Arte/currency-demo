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
package demo;

import currency.CurrencyChooser;
import currency.CurrencyPair;
import currency.MoneyAmount;
import currency.conversions.ConversionRateQuote;
import currency.conversions.CurrencyConverter;
import currency.conversions.ExchangeRateProvider;
import currency.conversions.MockExchangeRateProvider;
import currency.conversions.MockExchangeRateProviderNGTest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import static org.testframe.api.Asserters.assertThrows;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyConverterGUI class.
 * @author Alonso del Arte
 */
public class CurrencyConverterGUINGTest implements ItemListener {
    
    private static final ConversionRateQuote[] MOCK_QUOTES 
            = MockExchangeRateProviderNGTest.inventQuotes();
    
    private static final ExchangeRateProvider MOCK_RATE_PROVIDER 
            = new MockExchangeRateProvider(MOCK_QUOTES);
    
    private static final CurrencyConverter MOCK_CONVERTER 
            = new CurrencyConverter(MOCK_RATE_PROVIDER);
    
    private static final Set<Currency> ALLOWED_CURRENCIES 
            = MOCK_CONVERTER.getProvider().supportedCurrencies();
    
    private int itemChangeCallCounter = 0;
    
    @Override
    public void itemStateChanged(ItemEvent ie) {
        this.itemChangeCallCounter += ie.getStateChange();
        Currency item = (Currency) ie.getItem();
        System.out.println("Affected item is " + item.getDisplayName() + " (" 
                + item.getCurrencyCode() + ")");
    }
    
    @Test
    public void testGetPair() {
        System.out.println("getPair");
        Currency from = CurrencyChooser.chooseCurrency(ALLOWED_CURRENCIES);
        Currency to = CurrencyChooser.chooseCurrencyOtherThan(from, 
                ALLOWED_CURRENCIES);
        CurrencyPair expected = new CurrencyPair(from, to);
        CurrencyConverterGUI instance 
                = new CurrencyConverterGUI(expected, MOCK_CONVERTER);
        CurrencyPair actual = instance.getPair();
        assertEquals(actual, expected);
    }
    
    private static String defaultCloseOperationLabel(int code) {
        return switch (code) {
            case WindowConstants.DO_NOTHING_ON_CLOSE -> "DO NOTHING ON CLOSE";
            case WindowConstants.HIDE_ON_CLOSE -> "HIDE ON CLOSE";
            case WindowConstants.DISPOSE_ON_CLOSE -> "DISPOSE ON CLOSE";
            case WindowConstants.EXIT_ON_CLOSE -> "EXIT ON CLOSE";
            default -> "UNRECOGNIZED OPERATION CODE " + code;
        };
    }
    
    @Test
    public void testDefaultCloseOperation() {
        JFrame instance = new CurrencyConverterGUI(MOCK_CONVERTER);
        int expected = WindowConstants.EXIT_ON_CLOSE;
        int actual = instance.getDefaultCloseOperation();
        String message = "Expected " + defaultCloseOperationLabel(expected) 
                + ", got " + defaultCloseOperationLabel(actual);
        assertEquals(actual, expected, message);
    }
    
    // TODO: Rewind close operation and write constructor for 2-param instance
    
    @Test
    public void testConstructorRejectsFromPseudocurrency() {
        Currency from = CurrencyChooser.choosePseudocurrency();
        Currency to = CurrencyChooser.chooseCurrency(ALLOWED_CURRENCIES);
        CurrencyPair currencies = new CurrencyPair(from, to);
        String fromCurrCode = from.getCurrencyCode();
        String toCurrCode = to.getCurrencyCode();
        String msg = "Choosing " + from.getDisplayName() + " (" 
                + fromCurrCode + ") for conversion to " + to.getDisplayName() 
                + " (" + toCurrCode + ") should cause an exception";
        Throwable t = assertThrows(() -> {
            CurrencyConverterGUI instance = new CurrencyConverterGUI(currencies, 
                    MOCK_CONVERTER);
            System.out.println(msg + ", not created instance " 
                    + instance.toString());
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        String containsMsg = "Exception message should contain currency codes " 
                + fromCurrCode + " and " + toCurrCode;
        assert excMsg.contains(fromCurrCode) : containsMsg;
        assert excMsg.contains(toCurrCode) : containsMsg;
        System.out.println("\"" + excMsg + "\"");
    }
    
    @Test
    public void testConstructorRejectsToPseudocurrency() {
        Currency from = CurrencyChooser.chooseCurrency(ALLOWED_CURRENCIES);
        Currency to = CurrencyChooser.choosePseudocurrency();
        CurrencyPair currencies = new CurrencyPair(from, to);
        String fromCurrCode = from.getCurrencyCode();
        String toCurrCode = to.getCurrencyCode();
        String msg = "Choosing " + from.getDisplayName() + " (" 
                + fromCurrCode + ") for conversion to " + to.getDisplayName() 
                + " (" + toCurrCode + ") should cause an exception";
        Throwable t = assertThrows(() -> {
            CurrencyConverterGUI instance = new CurrencyConverterGUI(currencies, 
                    MOCK_CONVERTER);
            System.out.println(msg + ", not created instance " 
                    + instance.toString());
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        String containsMsg = "Exception message should contain currency codes " 
                + fromCurrCode + " and " + toCurrCode;
        assert excMsg.contains(fromCurrCode) : containsMsg;
        assert excMsg.contains(toCurrCode) : containsMsg;
        System.out.println("\"" + excMsg + "\"");
    }
    
}
