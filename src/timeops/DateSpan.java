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
import java.time.LocalDate;

/**
 * Represents a span of one or more days.
 * @author Alonso del Arte
 */
public class DateSpan implements Comparable<DateSpan>, DurationalSpan {
    
    private final LocalDate start, finish;
    
    // TODO: Write tests for this
    @Override
    public Duration getDuration() {
        return Duration.ZERO;
    }
    
    // TODO: Write tests for this
    @Override
    public int compareTo(DateSpan other) {
        return 0;
    }
    
    @Override
    public String toString() {
        return this.start.toString() + '\u2014' + this.finish.toString();
    }
    
    // TODO: Write tests for this
    @Override
    public String toASCIIString() {
        return "SORRY, NOT IMPLEMENTED YET";
    }
    
    public DateSpan(LocalDate begin, LocalDate end) {
        this.start = begin;
        this.finish = end;
    }
    
}
