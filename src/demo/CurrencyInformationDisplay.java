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
import currency.comparators.LetterCodeComparator;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
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
 * A display of information about a currency. The display shows the currency's 
 * name in the current locale (e.g., "Japanese Yen" in an English-speaking 
 * locale), the ISO-4217 letter code (e.g., "JPY"), the ISO-4217 numeric code 
 * (e.g., 392), the symbol in the current locale (may or may not differ from the 
 * letter code; e.g., &yen;), and the number of default fraction digits (e.g., 0 
 * in the case of the Japanese yen).
 * <p>The user may select a different currency from the combo box.</p>
 * @author Alonso del Arte
 */
public class CurrencyInformationDisplay extends JFrame implements ItemListener {
    
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
    
    static {
        Arrays.sort(ALL_SUITABLE_CURRENCIES, new LetterCodeComparator());
    }
    
    final JComboBox<Currency> currenciesDropdown 
            = new JComboBox<>(ALL_SUITABLE_CURRENCIES);
    
    final JLabel symbolLabel = new JLabel("Current locale symbol: ");
    
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
        this.displayNameField.setText(this.selectedCurrency.getDisplayName());
        this.letterCodeField.setText(this.selectedCurrency.getCurrencyCode());
        this.numberCodeField.setText(this.selectedCurrency
                .getNumericCodeAsString());
        this.symbolLabel.setEnabled(!this.selectedCurrency.getSymbol()
                .equals(this.selectedCurrency.getCurrencyCode()));
        this.symbolField.setText(this.selectedCurrency.getSymbol());
        this.fractionDigitsField.setText(Integer.toString(this.selectedCurrency
                .getDefaultFractionDigits()));
    }
    
    @Override
    public void itemStateChanged(ItemEvent ie) {
        if (ie.getStateChange() == ItemEvent.SELECTED) {
            this.setCurrency((Currency) ie.getItem());
        }
    }
    
    public void activate() {
        if (this.activated) {
            String excMsg = "Display was already activated";
            throw new IllegalStateException(excMsg);
        }
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.currenciesDropdown.addItemListener(this);
        this.pack();
        this.setVisible(true);
        this.activated = true;
    }
    
    // TODO: Mangle labels, write tests for labels, fields
    // TODO: Remove currency selection dropdown, write tests
    // TODO: Write Javadoc
    public CurrencyInformationDisplay(Currency currency) {
        this.selectedCurrency = currency;
        this.setTitle(PARTIAL_TITLE + currency.getCurrencyCode());
        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Choose "));
        this.currenciesDropdown.setSelectedItem(this.selectedCurrency);
        panel.add(this.currenciesDropdown);
        panel.add(new JLabel("Currency: "));
        this.displayNameField = new JTextField(this.selectedCurrency
                .getDisplayName(), DEFAULT_TEXT_FIELD_COLUMNS);
        this.displayNameField.setEditable(false);
        panel.add(this.displayNameField);
        panel.add(new JLabel("ISO-4217 letter code: "));
        this.letterCodeField =  new JTextField(this.selectedCurrency
                .getCurrencyCode());
        this.letterCodeField.setEditable(false);
        panel.add(this.letterCodeField);
        panel.add(new JLabel("Numeric code: "));
        this.numberCodeField = new JTextField(this.selectedCurrency
                .getNumericCodeAsString());
        this.numberCodeField.setEditable(false);
        panel.add(this.numberCodeField);
        this.symbolLabel.setEnabled(!this.selectedCurrency.getSymbol()
                .equals(this.selectedCurrency.getCurrencyCode()));
        panel.add(this.symbolLabel);
        this.symbolField = new JTextField(this.selectedCurrency.getSymbol());
        this.symbolField.setEditable(false);
        panel.add(this.symbolField);
        panel.add(new JLabel("Fraction digits: "));
        this.fractionDigitsField = new JTextField(Integer
                .toString(this.selectedCurrency.getDefaultFractionDigits()));
        this.fractionDigitsField.setEditable(false);
        panel.add(this.fractionDigitsField);
        this.add(panel);
    }
    
    public static void main(String[] args) {
        Currency dollars = Currency.getInstance(Locale.US);
        CurrencyInformationDisplay display 
                = new CurrencyInformationDisplay(dollars);
        display.activate();
    }
    
}
