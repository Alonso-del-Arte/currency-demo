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

import static currency.MoneyAmountNGTest.provideNull;

import java.time.Duration;
import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.testframe.api.Asserters.assertDoesNotThrow;
import static org.testframe.api.Asserters.assertThrows;
import static org.testframe.api.Asserters.assertZero;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the YearSpan class.
 * @author Alonso del Arte
 */
public class YearSpanNGTest {
    
    private static final Random RANDOM = new Random();
    
    private static final int ORIGIN_YEAR = 1800;
    
    private static final int BOUND_YEAR = 2200;
    
    private static final int DAYS_IN_A_NON_LEAP_YEAR = 365;
    
    private static final int DAYS_IN_A_LEAP_YEAR = DAYS_IN_A_NON_LEAP_YEAR + 1;
    
    private static final int DAYS_IN_TWO_NON_LEAP_YEARS 
            = 2 * DAYS_IN_A_NON_LEAP_YEAR;
    
    private static Year chooseYear() {
        int isoYear = RANDOM.nextInt(ORIGIN_YEAR, BOUND_YEAR);
        return Year.of(isoYear);
    }
    
    private static Year chooseYearAfter(Year year) {
        int origin = year.getValue() + 1;
        int bound = origin + 75;
        int isoYear = RANDOM.nextInt(origin, bound);
        return Year.of(isoYear);
    }
    
    private static Year chooseLeapYear() {
        int leapYear = 4 * RANDOM.nextInt(401, 599);
        int adjustment = (leapYear % 100 == 0) ? 4 : 0;
        return Year.of(leapYear + adjustment);
    }
    
    private static Year chooseNonLeapYear() {
        int leapYear = 4 * RANDOM.nextInt(400, 600);
        int nonLeapMod = RANDOM.nextInt(3) + 1;
        return Year.of(leapYear + nonLeapMod);
    }
    
    private static YearSpan makeYearSpan() {
        Year begin = chooseYear();
        Year end = chooseYearAfter(begin);
        return new YearSpan(begin, end);
    }
    
    @Test
    public void testToString() {
        System.out.println("toString");
        Year begin = chooseYear();
        Year end = chooseYearAfter(begin);
        YearSpan instance = new YearSpan(begin, end);
        String expected = begin.toString() + '\u2014' + end.toString();
        String actual = instance.toString();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testToStringSingleYear() {
        Year begin = chooseYear();
        YearSpan instance = new YearSpan(begin, begin);
        String expected = begin.toString();
        String actual = instance.toString();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testToASCIIString() {
        System.out.println("toASCIIString");
        Year begin = chooseYear();
        Year end = chooseYearAfter(begin);
        YearSpan instance = new YearSpan(begin, end);
        String expected = begin.toString() + " -- " + end.toString();
        String actual = instance.toASCIIString();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testToASCIIStringSingleYear() {
        Year begin = chooseYear();
        YearSpan instance = new YearSpan(begin, begin);
        String expected = begin.toString();
        String actual = instance.toASCIIString();
        assertEquals(actual, expected);
    }
    
    @Test
    public void testToHTMLString() {
        System.out.println("toHTMLString");
        Year begin = chooseYear();
        Year end = chooseYearAfter(begin);
        YearSpan instance = new YearSpan(begin, end);
        String expected = begin.toString() + "&mdash;" + end.toString();
        String actual = instance.toHTMLString().replace(" ", "");
        assertEquals(actual, expected);
    }
    
    @Test
    public void testToHTMLStringSingleYear() {
        Year begin = chooseYear();
        YearSpan instance = new YearSpan(begin, begin);
        String expected = begin.toString();
        String actual = instance.toHTMLString().replace(" ", "");
        assertEquals(actual, expected);
    }
    
    @Test
    public void testGetBeginYear() {
        System.out.println("getBeginYear");
        Year expected = chooseYear();
        Year end = chooseYearAfter(expected);
        YearSpan instance = new YearSpan(expected, end);
        Year actual = instance.getBeginYear();
        String message = "Getting begin year for " + instance.toString();
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testGetEndYear() {
        System.out.println("getEndYear");
        Year begin = chooseYear();
        Year expected = chooseYearAfter(begin);
        YearSpan instance = new YearSpan(begin, expected);
        Year actual = instance.getEndYear();
        String message = "Getting end year for " + instance.toString();
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testIsBefore() {
        System.out.println("isBefore");
        Year begin = chooseYear();
        Year end = chooseYearAfter(begin);
        YearSpan instance = new YearSpan(begin, end);
        Year year = chooseYearAfter(end);
        String msg = instance.toString() + " should be before " 
                + year.toString();
        assert instance.isBefore(year) : msg;
    }
    
    @Test
    public void testWithinIsNotBefore() {
        Year begin = chooseYear();
        Year end = chooseYearAfter(begin);
        YearSpan instance = new YearSpan(begin, end);
        Year stop = end.plusYears(1);
        for (Year year = begin; year.isBefore(stop); year = year.plusYears(1)) {
            String msg = instance.toString() 
                    + " should not be considered to be before " 
                    + year.toString();
            assert !instance.isBefore(year) : msg;
        }
    }
    
    @Test
    public void testAfterIsNotBefore() {
        Year year = chooseYear();
        Year begin = chooseYearAfter(year);
        Year end = chooseYearAfter(begin);
        YearSpan instance = new YearSpan(begin, end);
        String msg = instance.toString() + " should not be before " 
                + year.toString();
        assert !instance.isBefore(year) : msg;
    }
    
    @Test
    public void testBeforeIsNotIncluded() {
        Year begin = chooseYear();
        Year end = chooseYearAfter(begin);
        YearSpan instance = new YearSpan(begin, end);
        Year year = chooseYearAfter(end);
        String msg = instance.toString() + " should not include " 
                + year.toString();
        assert !instance.includes(year) : msg;
    }
    
    @Test
    public void testIncludes() {
        System.out.println("includes");
        Year begin = chooseYear();
        Year end = chooseYearAfter(begin);
        YearSpan instance = new YearSpan(begin, end);
        Year stop = end.plusYears(1);
        for (Year year = begin; year.isBefore(stop); year = year.plusYears(1)) {
            String msg = year.toString() + " should be within " 
                    + instance.toString();
            assert instance.includes(year) : msg;
        }
    }
    
    @Test
    public void testAfterIsNotIncluded() {
        Year year = chooseYear();
        Year begin = chooseYearAfter(year);
        Year end = chooseYearAfter(begin);
        YearSpan instance = new YearSpan(begin, end);
        String msg = instance.toString() + " should not include " 
                + year.toString();
        assert !instance.includes(year) : msg;
    }
    
    @Test
    public void testGetDurationSingleNonLeapYear() {
        Year begin = chooseNonLeapYear();
        YearSpan span = new YearSpan(begin, begin);
        Duration expected = Duration.ofDays(DAYS_IN_A_NON_LEAP_YEAR);
        Duration actual = span.getDuration();
        String message = "Reckoning duration of " + span.toString();
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testGetDurationTwoNonLeapsYears1To2Mod4() {
        Year begin = chooseLeapYear().plusYears(1);
        Year end = begin.plusYears(1);
        YearSpan span = new YearSpan(begin, end);
        Duration expected = Duration.ofDays(DAYS_IN_TWO_NON_LEAP_YEARS);
        Duration actual = span.getDuration();
        String message = "Reckoning duration of " + span.toString();
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testGetDurationTwoNonLeapsYears2To3Mod4() {
        Year begin = chooseLeapYear().plusYears(2);
        Year end = begin.plusYears(1);
        YearSpan span = new YearSpan(begin, end);
        Duration expected = Duration.ofDays(DAYS_IN_TWO_NON_LEAP_YEARS);
        Duration actual = span.getDuration();
        String message = "Reckoning duration of " + span.toString();
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testGetDurationThreeNonLeapsYears() {
        Year begin = chooseLeapYear().plusYears(1);
        Year end = begin.plusYears(2);
        YearSpan span = new YearSpan(begin, end);
        Duration expected = Duration.ofDays(3 * DAYS_IN_A_NON_LEAP_YEAR);
        Duration actual = span.getDuration();
        String message = "Reckoning duration of " + span.toString();
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testGetDurationFourYears() {
        Year begin = chooseYear();
        Year end = begin.plusYears(3);
        YearSpan span = new YearSpan(begin, end);
        int numberOfDays = 4 * DAYS_IN_A_NON_LEAP_YEAR + 1;
        Duration expected = Duration.ofDays(numberOfDays);
        Duration actual = span.getDuration();
        String message = "Reckoning duration of " + span.toString();
        assertEquals(actual, expected, message);
    }
    
    /**
     * Test of the getDuration function, of the YearSpan class.
     */
    @Test
    public void testGetDuration() {
        System.out.println("getDuration");
        int centuryYear = RANDOM.nextInt(16, 25) * 100;
        int isoYear = centuryYear + RANDOM.nextInt(1, 20);
        Year begin = Year.of(isoYear);
        isoYear += RANDOM.nextInt(60, 80);
        Year end = Year.of(isoYear);
        YearSpan instance = new YearSpan(begin, end);
        Year stop = end.plusYears(1);
        int numberOfYears = stop.getValue() - begin.getValue();
        int numberOfLeapDays = 0;
        for (Year curr = begin; curr.isBefore(stop); curr = curr.plusYears(1)) {
            if (curr.isLeap()) {
                numberOfLeapDays++;
            }
        }
        Duration expected 
                = Duration.ofDays(numberOfYears * DAYS_IN_A_NON_LEAP_YEAR 
                        + numberOfLeapDays);
        Duration actual = instance.getDuration();
        String message = "Reckoning duration of " + instance.toString();
        assertEquals(actual, expected, message);
    }
    
    // TODO: Write test for getDuration() of year span going across non-leap 
    // year century boundary, e.g., 1891 -- 1907, 2079 -- 2103
    
    // TODO: Write test for getDuration() of year span going across leap year 
    // century boundary, e.g., 1993 -- 2005
    
    @Test
    public void testReferentialEquality() {
        YearSpan instance = makeYearSpan();
        String message = "Span " + instance.toString() 
                + " should be equal to itself";
        assertEquals(instance, instance, message);
    }
    
    @Test
    public void testNotEqualsNull() {
        YearSpan instance = makeYearSpan();
        String msg = "Span " + instance.toString() + " should not equal null";
        Object obj = provideNull();
        assert !instance.equals(obj) : msg;
    }
    
    private static Object passThrough(Object obj) {
        return obj;
    }

    @Test
    public void testNotEqualsDiffClass() {
        Year year = chooseYear();
        YearSpan instance = new YearSpan(year, year);
        String msg = "Span " + instance.toString() 
                + " should not equal year object";
        Object obj = passThrough(year);
        assert !instance.equals(obj) : msg;
    }
    
    @Test
    public void testEquals() {
        System.out.println("equals");
        Year begin = chooseYear();
        Year end = chooseYearAfter(begin);
        YearSpan someSpan = new YearSpan(begin, end);
        YearSpan sameSpan = new YearSpan(begin, end);
        assertEquals(someSpan, sameSpan);
    }
    
    @Test
    public void testNotEqualsDiffBegin() {
        Year beginA = chooseYear();
        Year beginB = beginA.plusYears(1);
        Year end = chooseYearAfter(beginB);
        YearSpan spanA = new YearSpan(beginA, end);
        YearSpan spanB = new YearSpan(beginB, end);
        String msg = "Span " + spanA.toString() + " should not be equal to " 
                + spanB.toString();
        assert !spanA.equals(spanB) : msg;
    }

    @Test
    public void testNotEqualsDiffEnd() {
        Year begin = chooseYear();
        Year endA = chooseYearAfter(begin);
        Year endB = chooseYearAfter(endA);
        YearSpan spanA = new YearSpan(begin, endA);
        YearSpan spanB = new YearSpan(begin, endB);
        String msg = "Span " + spanA.toString() + " should not be equal to " 
                + spanB.toString();
        assert !spanA.equals(spanB) : msg;
    }
    
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Year begin = chooseYear();
        Year end = chooseYearAfter(begin);
        YearSpan instance = new YearSpan(begin, end);
        int expected = (begin.getValue() << 16) + end.getValue();
        int actual = instance.hashCode();
        String message = "Reckoning hash code for " + instance.toString();
        assertEquals(actual, expected, message);
    }

    /**
     * Test of the compareTo function, of the YearSpan class.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        List<YearSpan> expected = new ArrayList<>();
        int isoYear = RANDOM.nextInt(ORIGIN_YEAR - 100, ORIGIN_YEAR);
        Year begin = Year.of(isoYear);
        Year bound = Year.of(BOUND_YEAR);
        while (begin.isBefore(bound)) {
            Year end = chooseYearAfter(begin);
            YearSpan span = new YearSpan(begin, end);
            expected.add(span);
            begin = end;
        }
        List<YearSpan> actual = new ArrayList<>(expected);
        Collections.shuffle(actual);
        Collections.sort(actual);
        String message = "Year spans should be sorted as " 
                + expected.toString();
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testCompareToSameBeginYear() {
        int initialCapacity = RANDOM.nextInt(16) + 4;
        List<YearSpan> expected = new ArrayList<>(initialCapacity);
        Year begin = chooseYear();
        Year end = chooseYearAfter(begin);
        while (expected.size() < initialCapacity) {
            YearSpan span = new YearSpan(begin, end);
            expected.add(span);
            end = chooseYearAfter(end);
        }
        List<YearSpan> actual = new ArrayList<>(expected);
        Collections.shuffle(actual);
        Collections.sort(actual);
        String message = "Year spans should be sorted as " 
                + expected.toString();
        assertEquals(actual, expected, message);
    }
    
    @Test
    public void testCompareToSameBeginYearSameEndYear() {
        Year begin = chooseYear();
        Year end = chooseYearAfter(begin);
        YearSpan someSpan = new YearSpan(begin, end);
        YearSpan sameSpan = new YearSpan(begin, end);
        int actual = someSpan.compareTo(sameSpan);
        String msg = "Span " + someSpan.toString() + " should compare 0 to " 
                + sameSpan.toString();
        assertZero(actual, msg);
    }
    
    @Test
    public void testConstructorRejectsNullBeginYear() {
        Year end = chooseYear();
        String msg = "Null year and ending year " + end.toString() 
                + " should cause an exception";
        Throwable t = assertThrows(() -> {
            YearSpan badInstance = new YearSpan(null, end);
            System.out.println(msg + ", not created instance " 
                    + badInstance.toString());
        }, NullPointerException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
    
    @Test
    public void testConstructorRejectsNullEndYear() {
        Year begin = chooseYear();
        String msg = "Beginning year " + begin.toString() 
                + " and null ending year should cause an exception";
        Throwable t = assertThrows(() -> {
            YearSpan badInstance = new YearSpan(begin, null);
            System.out.println(msg + ", not created instance " 
                    + badInstance.toString());
        }, NullPointerException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        System.out.println("\"" + excMsg + "\"");
    }
    
    @Test
    public void testConstructorRejectsEndYearAfterBeginYear() {
        Year end = chooseYear();
        Year begin = chooseYearAfter(end);
        String msg = "Beginning year " + begin.toString() + " and ending year " 
                + end.toString() + " should cause an exception";
        Throwable t = assertThrows(() -> {
            YearSpan badInstance = new YearSpan(begin, end);
            System.out.println(msg + ", not created instance " 
                    + badInstance.toString());
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        String beginYearStr = Integer.toString(begin.getValue());
        String endYearStr = Integer.toString(end.getValue());
        String containsMsg = "Exception message should contain \"" 
                + beginYearStr + "\" and \"" + endYearStr + "\"";
        assert excMsg.contains(beginYearStr) : containsMsg;
        assert excMsg.contains(endYearStr) : containsMsg;
        System.out.println("\"" + excMsg + "\"");
    }
    
}
