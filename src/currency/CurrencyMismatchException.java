/*
 * Copyright (C) 2023 Alonso del Arte
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

import java.util.Currency;

/**
 * Signals that a currency mismatch has occurred and a currency conversion is 
 * needed. Note that this exception is different from the one in Joda Money.
 * @author Alonso del Arte
 */
public class CurrencyMismatchException extends RuntimeException {
    
    private static final long serialVersionUID = 4556099108573545874L;
        
    private final MoneyAmount amountA, amountB;
    
    /**
     * Getter for one of the amounts this exception was constructed with.
     * @return The first amount that was passed in to the constructor. For 
     * example, $349.75.
     */
    public MoneyAmount getAmountA() {
        return this.amountA;
    }

    /**
     * Getter for one of the amounts this exception was constructed with.
     * @return The second amount that was passed in to the constructor. For 
     * example, 500,00&euro;.
     */
    public MoneyAmount getAmountB() {
        return this.amountB;
    }
    
    private static String buildMessage(MoneyAmount amtA, MoneyAmount amtB) {
        Currency currencyA = amtA.getCurrency();
        Currency currencyB = amtB.getCurrency();
        return "Conversion needed from " + amtA.toString() + " to " 
                + currencyB.getDisplayName() + " (" 
                + currencyB.getCurrencyCode() + ") or " + amtB.toString() 
                + " to " + currencyA.getDisplayName() + " (" 
                + currencyA.getCurrencyCode() + ")";
    }

    public CurrencyMismatchException(MoneyAmount amtA, MoneyAmount amtB) {
        this(buildMessage(amtA, amtB), amtA, amtB);
    }
    
    public CurrencyMismatchException(String message, MoneyAmount amtA, 
            MoneyAmount amtB) {
        super(message);
        this.amountA = amtA;
        this.amountB = amtB;
    }

}
