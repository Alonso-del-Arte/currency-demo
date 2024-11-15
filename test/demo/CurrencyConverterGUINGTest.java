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
import currency.CurrencyPair;
import currency.ExchangeRateProvider;
import currency.MockExchangeRateProvider;
import currency.MoneyAmount;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import static org.testframe.api.Asserters.assertThrows;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the CurrencyConverterGUI class.
 * @author Alonso del Arte
 */
public class CurrencyConverterGUINGTest {
    
    private static final ExchangeRateProvider MOCK_RATE_PROVIDER 
            = new MockExchangeRateProvider();
    
    private static final CurrencyConverter MOCK_CONVERTER 
            = new CurrencyConverter(MOCK_RATE_PROVIDER);
    
    @Test
    public void testConstructorRejectsFromPseudocurrency() {
        Currency from = CurrencyChooser.choosePseudocurrency();
        Currency to = CurrencyChooser.chooseCurrency();
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
        Currency from = CurrencyChooser.chooseCurrency();
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
