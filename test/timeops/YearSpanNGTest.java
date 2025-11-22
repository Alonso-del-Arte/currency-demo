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
import java.util.Random;

import static org.testframe.api.Asserters.assertThrows;

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
    
    private Year chooseYear() {
        int isoYear = RANDOM.nextInt(ORIGIN_YEAR, BOUND_YEAR);
        return Year.of(isoYear);
    }
    
    /**
     * Test of getDuration method, of class YearSpan.
     */
//    @Test
    public void testGetDuration() {
        System.out.println("getDuration");
        YearSpan instance = null;
        Duration expResult = null;
//        Duration result = instance.getDuration();
//        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of compareTo method, of class YearSpan.
     */
//    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        YearSpan other = null;
        YearSpan instance = null;
        int expResult = 0;
//        int result = instance.compareTo(other);
//        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
    
}
