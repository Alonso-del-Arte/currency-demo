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
    
    private final Year start, finish;
    
    public Year getBeginYear() {
        return this.start;
    }
    
    public Year getEndYear() {
        return this.finish;
    }
    
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
    
    @Override
    public String toString() {
        if (this.start.equals(this.finish)) {
            return this.start.toString();
        }
        return this.start.toString() + '\u2014' + this.finish.toString();
    }
    
    public String toASCIIString() {
        if (this.start.equals(this.finish)) {
            return this.start.toString();
        }
        return this.start.toString() + " -- " + this.finish.toString();
    }
    
    /**
     * Determines if this year span object is equal to another object. For the 
     * examples, suppose this year span is 1991&mdash;1998.
     * @param obj The object to compare. Examples: a {@code Year} object for 
     * 1991, the year span 1989&mdash;1998, the year span 1991&mdash;1998, the 
     * year span 1991&mdash;2003, and null.
     * @return True only if {@code obj} is a {@code YearSpan} object with the 
     * same beginning year and the same ending year, false otherwise. In the 
     * examples, this would return true for 1991&mdash;1998, false for all 
     * others.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof YearSpan other) {
            return this.start.equals(other.start) 
                    && this.finish.equals(other.finish);
        } else {
            return false;
        }
    }
    
    /**
     * Gives a hash code for this year span. There is no attempt made to obscure 
     * the pertinent numbers.
     * @return A hash code which simply consists of the beginning year shifted 
     * sixteen bits to the right plus the ending year. For example, for 
     * 2001&mdash;2187, this would be 131139723.
     */
    @Override
    public int hashCode() {
        return (this.start.getValue() << 16) + this.finish.getValue();
    }
    
    public YearSpan(Year begin, Year end) {
        if (begin.isAfter(end)) {
            String excMsg = "Beginning year " + begin.toString() 
                    + " and ending year " + end.toString() + " are not valid";
            throw new IllegalArgumentException(excMsg);
        }
        this.start = begin;
        this.finish = end;
    }
    
}
