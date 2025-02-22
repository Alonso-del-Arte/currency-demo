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

import cacheops.LRUCache;
import currency.CurrencyChooser;
import currency.LocalesInfoGatherer;
import currency.comparators.LetterCodeComparator;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import ui.CurrencyWrapper;
import ui.graphical.CurrencyDropdownMenu;

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
     * The preferred width for the size of this graphical component. This is a 
     * value I've arrived at through a lot of experimentation. Subject to change 
     * in later versions of this program.
     */
    static final int DEFAULT_WIDTH = 600;
    
    /**
     * The preferred height for the size of this graphical component. This is a 
     * value I've arrived at through a lot of experimentation. Subject to change 
     * in later versions of this program.
     */
    static final int DEFAULT_HEIGHT = 400;
    
    /**
     * The preferred size for this graphical component. It is equal to {@link 
     * #DEFAULT_WIDTH} times {@link #DEFAULT_HEIGHT}.
     */
    private static final Dimension DEFAULT_DIMENSION 
            = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    
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
    
    final JComboBox<CurrencyWrapper> currenciesDropdown 
            = new CurrencyDropdownMenu(ALL_SUITABLE_CURRENCIES);
    
    final JLabel symbolLabel = new JLabel("Current locale symbol: ");
    
    JTextField displayNameField, letterCodeField, numberCodeField, symbolField,  
            fractionDigitsField;
    
    final JTextArea otherDisplayNames, otherSymbols;
    
    private boolean activated = false;
    
    private Currency selectedCurrency;
    
    private static final int DEFAULT_NUMBER_OF_CACHED_LOCALES_INFO_GATHERERS 
            = 32;
    
    final LRUCache<Currency, LocalesInfoGatherer> localesInfoCache 
            = new LRUCache<Currency, LocalesInfoGatherer>
                (DEFAULT_NUMBER_OF_CACHED_LOCALES_INFO_GATHERERS) {
                    
                    @Override
                    public LocalesInfoGatherer create(Currency currency) {
                        return new LocalesInfoGatherer(currency);
                    }
                
                };
    
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
        LocalesInfoGatherer locsInfo = this.localesInfoCache
                .retrieve(this.selectedCurrency);
        Set<String> moreDisplayNames = locsInfo.getDisplayNames().keySet();
        moreDisplayNames.remove(this.selectedCurrency.getDisplayName());
        this.otherDisplayNames.setText(moreDisplayNames.toString());
        Set<String> moreSymbols = locsInfo.getSymbols().keySet();
        moreSymbols.remove(this.selectedCurrency.getSymbol());
        this.otherSymbols.setText(moreSymbols.toString());
    }
    
    @Override
    public void itemStateChanged(ItemEvent ie) {
        if (ie.getStateChange() == ItemEvent.SELECTED) {
            this.setCurrency(((CurrencyWrapper) ie.getItem())
                    .getWrappedCurrency());
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
        this.setPreferredSize(DEFAULT_DIMENSION);
        JPanel panel = new JPanel(new GridLayout(9, 2));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Choose "));
        this.currenciesDropdown
                .setSelectedItem(new CurrencyWrapper(this.selectedCurrency));
        panel.add(this.currenciesDropdown);
        panel.add(new JLabel("Currency: "));
        String mainDisplayName = this.selectedCurrency.getDisplayName();
        this.displayNameField = new JTextField(mainDisplayName, 
                DEFAULT_TEXT_FIELD_COLUMNS);
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
        String mainSymbol = this.selectedCurrency.getSymbol();
        this.symbolField = new JTextField(mainSymbol);
        this.symbolField.setEditable(false);
        panel.add(this.symbolField);
        panel.add(new JLabel("Fraction digits: "));
        this.fractionDigitsField = new JTextField(Integer
                .toString(this.selectedCurrency.getDefaultFractionDigits()));
        this.fractionDigitsField.setEditable(false);
        panel.add(this.fractionDigitsField);
        LocalesInfoGatherer locsInfo 
                = this.localesInfoCache.retrieve(this.selectedCurrency);
        Set<String> moreDisplayNames = locsInfo.getDisplayNames().keySet();
        moreDisplayNames.remove(mainDisplayName);
        panel.add(new JLabel("Other display names: "));
        this.otherDisplayNames = new JTextArea(10, DEFAULT_TEXT_FIELD_COLUMNS);
        this.otherDisplayNames.setLineWrap(true);
        this.otherDisplayNames.setText(moreDisplayNames.toString());
        JScrollPane scrollPane1 = new JScrollPane(this.otherDisplayNames);
        panel.add(scrollPane1);
        panel.add(new JSeparator());
        panel.add(new JSeparator());
        panel.add(new JLabel("Other symbols: "));
        Set<String> moreSymbols = locsInfo.getSymbols().keySet();
        moreSymbols.remove(mainSymbol);
        this.otherSymbols = new JTextArea(3, DEFAULT_TEXT_FIELD_COLUMNS);
        this.otherSymbols.setLineWrap(true);
        this.otherSymbols.setText(moreSymbols.toString());
        JScrollPane scrollPane2 = new JScrollPane(this.otherSymbols);
        panel.add(scrollPane2);
        this.add(panel);
    }
    
    public static void main(String[] args) {
        Currency dollars = Currency.getInstance(Locale.US);
        CurrencyInformationDisplay display 
                = new CurrencyInformationDisplay(dollars);
        display.activate();
    }
    
}
