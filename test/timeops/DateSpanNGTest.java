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

import static org.testng.Assert.*;
import org.testng.annotations.Test;

import static timeops.YearSpanNGTest.RANDOM;

/**
 * Tests of the DateSpan class.
 * @author Alonso del Arte
 */
public class DateSpanNGTest {
    
    private static final LocalDate TEST_BEGIN_DATE = LocalDate.now();
    
    private static LocalDate chooseDate() {
        int daysToSubtract = RANDOM.nextInt(2, 2048);
        return TEST_BEGIN_DATE.minusDays(daysToSubtract);
    }
    
    private static LocalDate chooseDateAfter(LocalDate date) {
        int daysToAdd = RANDOM.nextInt(2, 2048);
        return date.plusDays(daysToAdd);
    }
    
    /**
     * Test of getDuration method, of class DateSpan.
     */
    @Test
    public void testGetDuration() {
        System.out.println("getDuration");
        DateSpan instance = null;
        Duration expResult = null;
//        Duration result = instance.getDuration();
//        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of compareTo method, of class DateSpan.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        DateSpan other = null;
        DateSpan instance = null;
        int expResult = 0;
//        int result = instance.compareTo(other);
//        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
