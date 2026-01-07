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
package timeops;

import java.time.Duration;

/**
 * Represents a span made up of discrete units of time, such as years or hours.
 * @author Alonso del Arte
 */
public interface DurationalSpan {
    
    /**
     * Gives the duration of the span.
     * @return The duration. For example, if this span represents the month of 
     * February in a non-leap year, the duration should be 28 days.
     */
    Duration getDuration();
    
    /**
     * Gives a text representation of this durational span using only ASCII 
     * characters. May be the same as {@code toString()} provided that one 
     * doesn't use characters beyond ASCII.
     * @return A text representation. Might use, for example, two dashes instead 
     * of an em dash.
     */
    String toASCIIString();
    
}
