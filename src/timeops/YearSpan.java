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
import java.time.format.DateTimeParseException;

/**
 * Represents a span of one or more years. This class is intended for use by 
 * {@link currency.comparators.HistoricalCurrenciesComparator} to sort 
 * historical currencies with known years of validity.
 * <p>A year span technically begins on January 1 of the beginning year and ends 
 * on December 31 of the ending year. A finer-grained {@link DurationalSpan} is 
 * needed for different starting and/or ending dates.</p>
 * @author Alonso del Arte
 */
public class YearSpan implements Comparable<YearSpan>, DurationalSpan {
    
    private static final int DAYS_IN_A_NON_LEAP_YEAR = 365;
    
    private static final int DAYS_IN_A_LEAP_YEAR = DAYS_IN_A_NON_LEAP_YEAR + 1;
    
    private static final int DAYS_IN_TWO_NON_LEAP_YEARS 
            = 2 * DAYS_IN_A_NON_LEAP_YEAR;
    
    private final Year start, finish;
    
    /**
     * Retrieves the beginning year for this span. For the example, suppose this 
     * year span is 1871 &mdash; 2008.
     * @return The beginning year. For example, 1871. Never null.
     */
    public Year getBeginYear() {
        return this.start;
    }
    
    /**
     * Retrieves the ending year for this span. For the example, suppose this 
     * year span is 1871 &mdash; 2008.
     * @return The ending year. For example, 2008. Never null.
     */
    public Year getEndYear() {
        return this.finish;
    }
    
    /**
     * Determines whether or not this span is before a given year. For the 
     * examples, suppose this span is 1994 &mdash; 2002.
     * @param year The year for which to make the determination. Examples: 2025, 
     * 2013, 2002, 1998, 1994, 1873, 1729.
     * @return True if this span is before {@code year}, or in other words, 
     * {@code year} is after the end of this year span. False otherwise. In the 
     * examples, true for 2025 and 2013, false for 2002 even though that's the 
     * end of the example year span, and obviously false for 1998, 1994 and 
     * 1873.
     */
    public boolean isBefore(Year year) {
        return year.isAfter(this.finish);
    }
    
    /**
     * Determines whether or not this span includes a given year. For the 
     * examples, suppose this span is 1994 &mdash; 2002.
     * @param year The year for which to make the determination. Examples: 1994,  
     * 1998, 2002, 1729, 1873, 2013, 2025.
     * @return True if this span includes {@code year} as the beginning year, 
     * ending year or any year in between. False otherwise. In the examples, 
     * true for 1994, 1998 and 2002, false for 1729, 1873, 2013, 2025.
     */
    public boolean includes(Year year) {
        return year.compareTo(this.start) > -1 
                && year.compareTo(this.finish) < 1;
    }
    
    // TODO: Write tests for this
    public boolean isAfter(Year year) {
        return false;
    }
    
    // TODO: Write tests for this accounting for century non-leap years, e.g., 
    // 1900, 2100
    // TODO: Once this is passing all the pertinent tests, amend the {@link 
    // #compareTo()} Javadoc to include the following:
//    To compare 
//     * year spans by duration, use the {@code compareTo()} function of the 
//     * {@code Duration} objects returned by {@link #getDuration()}.
    @Override
    public Duration getDuration() {
        int numberOfYears = this.finish.getValue() - this.start.getValue() + 1;
        int leapDays = 0;
        Year stop = this.finish.plusYears(1);
        for (Year curr = this.start; curr.isBefore(stop); 
                curr = curr.plusYears(1)) {
            if (curr.isLeap()) {
                leapDays++;
            }
        }
        return Duration.ofDays(numberOfYears * DAYS_IN_A_NON_LEAP_YEAR 
                + leapDays);
    }
    
    // TODO: Write tests for this
    public static YearSpan parse(String s) {
        return new YearSpan(Year.of(-7000), Year.of(6000));
    }
    
    /**
     * Compares this year span to another year span. Note that the ending years 
     * are considered only if the beginning years are the same. Thus a longer 
     * span will be considered "less" than a short span that begins later. For 
     * the examples, suppose this year span is 1871 &mdash; 2008. 
     * @param other The span to compare. Examples: 1871 &mdash; 2008, 1871 
     * &mdash; 1898, 1994 &mdash; 2008.
     * @return 0 if this year span begins and ends on the same years as {@code 
     * other}; &minus;1 or any other negative integer if {@code other} begins on 
     * a later year, or they begin on the same year but this span ends earlier; 
     * 1 or any other positive integer if {@code other} begins on an earlier 
     * year, or if they begin on the same year but this span ends later. In the 
     * examples, this function would return 0 for 1871 &mdash; 2008 because the 
     * beginning and ending years are the same; it might return 110 or any other 
     * positive integer for 1871 &mdash; 1898 (beginning years are the same but 
     * the ending years are 110 years apart); it might return &minus;123 or any 
     * other negative integer for 1994 &mdash; 2008 as the beginning years are 
     * 123 years apart (that the ending years are the same is not taken into 
     * account when the beginning years are different).
     */
    @Override
    public int compareTo(YearSpan other) {
        int comparison = this.start.compareTo(other.start);
        if (comparison == 0) {
            return this.finish.compareTo(other.finish);
        } else return comparison;
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
    
    public String toHTMLString() {
        if (this.start.equals(this.finish)) {
            return this.start.toString();
        }
        return this.start.toString() + " &mdash; " + this.finish.toString();
    }
    
    /**
     * Determines if this year span object is equal to another object. For the 
     * examples, suppose this year span is 1991 &mdash; 1998.
     * @param obj The object to compare. Examples: a {@code Year} object for 
     * 1991, the year span 1989 &mdash; 1998, the year span 1991 &mdash; 1998,  
     * the year span 1991 &mdash; 2003, and null.
     * @return True only if {@code obj} is a {@code YearSpan} object with the 
     * same beginning year and the same ending year, false otherwise. In the 
     * examples, this would return true for 1991 &mdash; 1998, false for all 
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
     * 2001 &mdash; 2187, this would be 131139723.
     */
    @Override
    public int hashCode() {
        return (this.start.getValue() << 16) + this.finish.getValue();
    }
    
    /**
     * Sole constructor.
     * @param begin The beginning year. For example, 1968.
     * @param end The ending year. For example, 2012. This may be the same as 
     * {@code begin} but it must not be earlier.
     * @throws IllegalArgumentException If the ending year is prior to the 
     * beginning year.
     * @throws NullPointerException If either {@code begin} or {@code end} is 
     * null.
     */
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
