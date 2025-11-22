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
package timeops;

import java.time.Duration;
import java.time.Year;

/**
 * Represents a span of one or more years.
 * @author Alonso del Arte
 */
public class YearSpan implements Comparable<YearSpan>, DurationalSpan {
    
    // TODO: Write tests for this
    @Override
    public Duration getDuration() {
        return Duration.ZERO;
    }
    
    // TODO: Write tests for this
    @Override
    public int compareTo(YearSpan other) {
        return 0;
    }
    
    public YearSpan(Year begin, Year end) {
        if (begin == null) {
            String excMsg = "Beginning year must not be null";
            throw new NullPointerException(excMsg);
        }
        if (end == null) {
            String excMsg = "Ending year must not be null";
            throw new NullPointerException(excMsg);
        }
    }
    
}
