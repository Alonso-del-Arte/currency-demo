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
package ui.graphical;

import java.awt.GridLayout;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import static org.testframe.api.Asserters.assertContainsSame;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

import ui.CurrencyWrapper;

/**
 * Tests of the CurrencyDropdownMenu class.
 * @author Alonso del Arte
 */
public class CurrencyDropdownMenuNGTest {
    
    private static final Random RANDOM = new Random(System.nanoTime());
    
    private static final Set<Currency> CURRENCIES 
            = Currency.getAvailableCurrencies();
    
    private static final int TOTAL_NUMBER_OF_CURRENCIES = CURRENCIES.size();
    
    private static final Currency[] CURRENCIES_ARRAY 
            = CURRENCIES.toArray(Currency[]::new);
    
    private static Currency[] makeArray(int size) {
        Currency[] array = new Currency[size];
        int bound = TOTAL_NUMBER_OF_CURRENCIES / size;
        for (int i = 0; i < size; i++) {
            int index = i * bound + RANDOM.nextInt(bound);
            array[i] = CURRENCIES_ARRAY[index];
        }
        return array;
    }
    
    @Test
    public void testConstructorWrapsCurrencies() {
        int size = 10;
        Currency[] currencies = makeArray(size);
        JComboBox<CurrencyWrapper> instance 
                = new CurrencyDropdownMenu(currencies);
        List<CurrencyWrapper> expected = Arrays.asList(currencies).stream()
                .map((cur) -> new CurrencyWrapper(cur))
                .collect(Collectors.toList());
        int initialCapacity = instance.getItemCount();
        String capMsg = "Drop-down should have " + size 
                + " items passed to the constructor";
        assert size == initialCapacity : capMsg;
        List<CurrencyWrapper> actual = new ArrayList<>(initialCapacity);
        for (int index = 0; index < initialCapacity; index++) {
            actual.add(instance.getItemAt(index));
        }
        assertContainsSame(expected, actual);
    }
    
}
