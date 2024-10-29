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
package currency;

import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Gathers information about one currency in every locale recognized by the Java 
 * Runtime Environment (JRE). The information is all available by polling {@code 
 * Currency} instances, but that's laborious enough that it's worth creating a 
 * program to gather that information.
 * @author Alonso del Arte
 */
public class LocalesInfoGatherer {
    
    // TODO: Write tests for this
    public Currency getCurrency() {
        return Currency.getInstance("XTS");
    }
    
    // TODO: Write tests for this
    public Map<String, Set<Locale>> getSymbols() {
        return new HashMap<>();
    }
    
    // TODO: Write tests for this
    public Map<String, Set<Locale>> getDisplayNames() {
        return new HashMap<>();
    }
    
    // TODO: Write tests for this
    public LocalesInfoGatherer(Currency currency) {
        //
    }
    
}
