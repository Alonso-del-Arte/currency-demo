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

import currency.ConversionRateQuote;
import currency.CurrencyConverter;
import currency.CurrencyPair;
import currency.MannysCurrencyConverterAPIAccess;
import currency.MoneyAmount;
import currency.RateQuoteCache;

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

/**
 *
 * @author Alonso del Arte
 */
public class CurrencyConverterGUI extends JFrame implements ActionListener, 
        ItemListener {
    
    private static final Currency DOLLARS = Currency.getInstance(Locale.US);
    
    private static final Currency EUROS = Currency.getInstance("EUR");
    
    private static final CurrencyPair DEFAULT_PAIR = new CurrencyPair(DOLLARS, 
            EUROS);
    
    private static final Currency[] ALL_CURRENCIES 
            = Currency.getAvailableCurrencies().toArray(Currency[]::new);
    
    private final JComboBox<Currency> fromCurrencies 
            = new JComboBox<>(ALL_CURRENCIES);
    
    private final JComboBox<Currency> toCurrencies 
            = new JComboBox<>(ALL_CURRENCIES);
    
    private final JTextField numberField = new JTextField(10);
    
    private JLabel fromCurrencyNameReadout 
            = new JLabel("SORRY, NOT IMPLEMENTED YET");
    
    private JLabel toCurrencyNameReadout 
            = new JLabel("SORRY, NOT IMPLEMENTED YET");
    
    private JLabel fromReadout = new JLabel("SORRY, NOT IMPLEMENTED YET");
    
    private JLabel toReadout = new JLabel("SORRY, NOT IMPLEMENTED YET");
    
    private Currency fromCurrency, toCurrency;
    
    private MoneyAmount fromAmount, toAmount;
    
    private RateQuoteCache quoteCache = new RateQuoteCache(10) {
        
        // TODO: Write tests for this
        @Override
        public boolean needsRefresh(CurrencyPair currencies) {
            return true;
        }
        
        // TODO: Write tests for this
        @Override
        protected ConversionRateQuote create(CurrencyPair name) {
            return new ConversionRateQuote(name, 1.0);
        }
        
    };
    
    private void updateReadouts() {
        //
    }

    @Override
    public void itemStateChanged(ItemEvent ie) {
        //
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        //
    }
    
    public CurrencyConverterGUI(CurrencyConverter converter) {
        this(DEFAULT_PAIR, converter);
    }

    public CurrencyConverterGUI(CurrencyPair currencies, 
            CurrencyConverter converter) {
        Currency from = currencies.getFromCurrency();
        Currency to = currencies.getToCurrency();
        boolean eitherIsPseudo = from.getDefaultFractionDigits() < 0 
                || to.getDefaultFractionDigits() < 0;
        if (eitherIsPseudo) {
            String excMsg = "Combination of currency from " 
                    + from.getCurrencyCode() + " to " + to.getCurrencyCode() 
                    + " is not valid for this converter";
            throw new IllegalArgumentException(excMsg);
        }
        this.fromCurrency = from;
        this.toCurrency = to;
        this.fromAmount = new MoneyAmount(1, this.fromCurrency);
        this.updateReadouts();
        String title = "Currency conversion from " 
                + this.fromCurrency.getCurrencyCode() + " to " 
                + this.toCurrency.getCurrencyCode();
        this.setTitle(title);
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("From: "));
        inputPanel.add(this.numberField);
        inputPanel.add(this.fromCurrencies);
        inputPanel.add(new JLabel("To: "));
        inputPanel.add(this.toCurrencies);
        this.add(inputPanel, BorderLayout.PAGE_START);
        JPanel namesPanel = new JPanel(new BorderLayout(20, 20));
//        this.fromCurrencyNameReadout.setText(from.getDisplayName());
        namesPanel.add(this.fromCurrencyNameReadout, BorderLayout.PAGE_START);
        namesPanel.add(this.toCurrencyNameReadout, BorderLayout.PAGE_END);
        this.add(namesPanel, BorderLayout.CENTER);
        JPanel readoutsPanel = new JPanel();
        readoutsPanel.add(this.fromReadout);
        readoutsPanel.add(new JLabel(" exchanges to "));
        readoutsPanel.add(this.toReadout);
        this.add(readoutsPanel, BorderLayout.PAGE_END);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
    }
    
    public static void main(String[] args) {
        CurrencyConverterGUI display = new CurrencyConverterGUI(DEFAULT_PAIR, 
                new CurrencyConverter(new MannysCurrencyConverterAPIAccess()));
        display.setVisible(true);
    }
    
}
