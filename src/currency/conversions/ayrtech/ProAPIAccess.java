/*
 * Copyright (C) 2025 Alonso del Arte
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
package currency.conversions.ayrtech;

import currency.CurrencyPair;
import currency.SpecificCurrenciesSupport;
import currency.conversions.ExchangeRateProvider;

import java.time.LocalDate;
import java.time.Month;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

/**
 * WORK IN PROGRESS... The Pro API costs $10 a month
 * @author Alonso del Arte
 */
public class ProAPIAccess implements ExchangeRateProvider, 
        SpecificCurrenciesSupport {
    
    // TODO: Write tests for this
    @Override
    public Set<Currency> supportedCurrencies() {
        return new HashSet<>();
    }
    
    // TODO: Write tests for this
    @Override
    public double getRate(Currency source, Currency target) {
        return -1.0;
    }
    
}
