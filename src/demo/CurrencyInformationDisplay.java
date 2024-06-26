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

import java.awt.ItemSelectable;
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
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Alonso del Arte
 */
public class CurrencyInformationDisplay extends JFrame 
        implements ActionListener, ItemListener {
    
    private static final String PARTIAL_TITLE = "Currency Information for ";
    
    /**
     * How many columns wide to make the text field for the currency's display 
     * name field. The display name can be as short as four letters (e.g., the 
     * euro) or it can be as long as that of the S&atilde;o Tom&eacute; and 
     * Pr&iacute;ncipe dobra or longer still, in which case it generally won't 
     * fit in the default column width.
     */
    static final int DEFAULT_TEXT_FIELD_COLUMNS = 16;
    
    private static final Currency[] ALL_SUITABLE_CURRENCIES 
            = CurrencyChooser.getSuitableCurrencies().toArray(Currency[]::new);
    
    private final JComboBox<Currency> currencies 
            = new JComboBox<>(ALL_SUITABLE_CURRENCIES);
    
    JTextField displayNameField, letterCodeField, numberCodeField, symbolField,  
            fractionDigitsField;
    
    private boolean activated = false;
    
    private Currency selectedCurrency;
    
    public Currency getCurrency() {
        return this.selectedCurrency;
    }
    
    public void setCurrency(Currency currency) {
        this.selectedCurrency = currency;
        this.setTitle(PARTIAL_TITLE + this.selectedCurrency.getCurrencyCode());
    }
    
    @Override
    public void itemStateChanged(ItemEvent ie) {
        //
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
//        String command = ae.getActionCommand();
//        switch (command) {
//            case NINE_PIPS_LABEL:
//                CardImage.toggleNinePipsAreSymmetrical();
//                this.repaint();
//                break;
//            default:
//                System.err.println("Command \"" + command 
//                        + "\" not recognized");
//        }
    }
    
    public void activate() {
        if (this.activated) {
            String excMsg = "Display was already activated";
            throw new IllegalStateException(excMsg);
        }
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.activated = true;
    }
    
    public CurrencyInformationDisplay(Currency currency) {
        this.selectedCurrency = currency;
        this.setTitle(PARTIAL_TITLE + currency.getCurrencyCode());
        JPanel panel = new JPanel();
        String text = "Huh?";
        this.displayNameField = new JTextField(this.selectedCurrency
                .getDisplayName(), DEFAULT_TEXT_FIELD_COLUMNS);
        panel.add(this.displayNameField);
        panel.add(new JLabel(text));
        this.displayNameField.setEditable(false);
        this.letterCodeField =  new JTextField(this.selectedCurrency
                .getCurrencyCode());
        this.letterCodeField.setEditable(false);
        this.numberCodeField = new JTextField(this.selectedCurrency
                .getNumericCodeAsString());
        this.numberCodeField.setEditable(false);
        this.symbolField = new JTextField(text);
        this.symbolField.setEditable(false);
        this.fractionDigitsField = new JTextField(text);
        this.fractionDigitsField.setEditable(false);
        this.add(panel);
    }
    
    public static void main(String[] args) {
        Currency dollars = Currency.getInstance(Locale.US);
        CurrencyInformationDisplay display 
                = new CurrencyInformationDisplay(dollars);
        display.activate();
    }
    
}
