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
package cacheops;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.regex.Pattern;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the LRUCache class.
 * @author Alonso del Arte
 */
public class LRUCacheNGTest {
    
    @Test
    public void testMinimumCapacityConstant() {
        int expected = 4;
        int actual = LRUCache.MINIMUM_CAPACITY;
        assertEquals(actual, expected);
    }
    
    @Test
    public void testMaximumCapacityConstant() {
        int expected = 128;
        int actual = LRUCache.MAXIMUM_CAPACITY;
        assertEquals(actual, expected);
    }
    
    private static class LRUCacheImpl extends LRUCache<String, Pattern> {

        @Override
        protected Pattern create(String name) {
            return Pattern.compile(name);
        }

        public LRUCacheImpl(int capacity) {
            super(capacity);
        }
        
    }
    
}
