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

import java.util.Random;
import java.util.regex.Pattern;

import static org.testframe.api.Asserters.assertThrows;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Tests of the LRUCache class.
 * @author Alonso del Arte
 */
public class LRUCacheNGTest {
    
    private static final Random RANDOM = new Random();
    
    private static final int CAPACITY_ORIGIN = 4;
    
    private static final int CAPACITY_BOUND = 129;
    
    private static int chooseCapacity() {
        return RANDOM.nextInt(CAPACITY_ORIGIN, CAPACITY_BOUND);
    }
    
    private static String makeRegexNameForNumberWithDash() {
        int a = RANDOM.nextInt(2, 10);
        int b = RANDOM.nextInt(2, 10);
        return "\\d{" + a + "}-\\d{" + b + "}";
    }
    
    private static String makeRegexNameForCapitalizedWord() {
        int n = RANDOM.nextInt(2, 10);
        return "[A-Z][a-z]{" + n + "}";
    }
    
    @Test
    public void testMinimumCapacityConstant() {
        int expected = CAPACITY_ORIGIN;
        int actual = LRUCache.MINIMUM_CAPACITY;
        assertEquals(actual, expected);
    }
    
    @Test
    public void testMaximumCapacityConstant() {
        int expected = CAPACITY_BOUND - 1;
        int actual = LRUCache.MAXIMUM_CAPACITY;
        assertEquals(actual, expected);
    }
    
    @Test
    public void testConstructorRejectsNegativeCapacity() {
        int capacity = RANDOM.nextInt() | Integer.MIN_VALUE;
        String msg = "Capacity " + capacity + " should cause exception";
        Throwable t = assertThrows(() -> {
            LRUCache<String, Pattern> badInstance = new LRUCacheImpl(capacity);
            System.out.println(msg + ", not created " + badInstance.toString());
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        String numStr = Integer.toString(capacity);
        String capMsg = "Exception message should include \"" + numStr + "\"";
        assert excMsg.contains(numStr) : capMsg;
        System.out.println("\"" + excMsg + "\"");
    }

    @Test
    public void testConstructorRejectsSizeBelowMinimum() {
        for (int i = 0; i < LRUCache.MINIMUM_CAPACITY; i++) {
            final int badSize = i;
            Throwable t = assertThrows(() -> {
                LRUCache<String, Pattern> badCache = new LRUCacheImpl(badSize);
                System.out.println("Should not have been able to create "
                        + badCache + " of size " + badSize
                        + ", which is less than minimum capacity "
                        + LRUCache.MINIMUM_CAPACITY);
            }, IllegalArgumentException.class);
            String excMsg = t.getMessage();
            assert excMsg != null : "Exception message should not be null";
            assert !excMsg.isBlank() : "Exception message should not be blank";
            String numStr = Integer.toString(badSize);
            String capMsg = "Exception message should include \"" + numStr 
                    + "\"";
            assert excMsg.contains(numStr) : capMsg;
            System.out.println("\"" + excMsg + "\"");
        }
    }

    @Test
    public void testConstructorRejectsExcessiveCapacity() {
        int capacity = LRUCache.MAXIMUM_CAPACITY 
                + RANDOM.nextInt(Short.MAX_VALUE) + 1;
        String msg = "Capacity " + capacity + " should cause exception";
        Throwable t = assertThrows(() -> {
            LRUCache<String, Pattern> badInstance = new LRUCacheImpl(capacity);
            System.out.println(msg + ", not created " + badInstance.toString());
        }, IllegalArgumentException.class, msg);
        String excMsg = t.getMessage();
        assert excMsg != null : "Exception message should not be null";
        assert !excMsg.isBlank() : "Exception message should not be blank";
        String numStr = Integer.toString(capacity);
        String capMsg = "Exception message should include \"" + numStr + "\"";
        assert excMsg.contains(numStr) : capMsg;
        System.out.println("\"" + excMsg + "\"");
    }
    
    @Test
    public void testDoesNotHave() {
        int capacity = chooseCapacity();
        LRUCache<String, Pattern> instance = new LRUCacheImpl(capacity);
        String name = makeRegexNameForNumberWithDash();
        Pattern value = Pattern.compile(name);
        String msg = "Cache shouldn't have value " + value.toString() 
                + " that wasn't added";
        assert !instance.has(value) : msg;
    }
    
    @Test
    public void testHas() {
        System.out.println("has");
        int capacity = chooseCapacity();
        LRUCache<String, Pattern> instance = new LRUCacheImpl(capacity);
        String name = makeRegexNameForNumberWithDash();
        Pattern value = instance.retrieve(name);
        String msg = "Cache should have value " + value.toString() 
                + " that was added";
        assert instance.has(value) : msg;
    }
    
    @Test
    public void testStillHas() {
        int capacity = chooseCapacity();
        LRUCache<String, Pattern> instance = new LRUCacheImpl(capacity);
        String name = makeRegexNameForCapitalizedWord();
        Pattern value = instance.retrieve(name);
        for (int i = 1; i < capacity; i++) {
            instance.retrieve(makeRegexNameForNumberWithDash());
        }
        String msg = "Cache should still have value " + value.toString()
                + " in cache with capacity " + capacity 
                + " that hasn't had any evictions";
        assert instance.has(value) : msg;
    }
    
    /**
     * Test of the retrieve function, of the LRUCache class. Many name-value 
     * pairs can be added to the cache, and as long as there's capacity, all 
     * values should be available for retrieval. The retrieve call should not 
     * cause a create call for a value that's already in the cache. Note that 
     * the Pattern class does not have an equals() override, at least as of Java 
     * 21.
     */
    @Test
    public void testRetrieve() {
        System.out.println("retrieve");
        int capacity = chooseCapacity();
        LRUCacheImpl instance = new LRUCacheImpl(capacity);
        String name = makeRegexNameForCapitalizedWord();
        Pattern expected = instance.retrieve(name);
        for (int i = 1; i < capacity; i++) {
            instance.retrieve(makeRegexNameForNumberWithDash());
        }
        Pattern actual = instance.retrieve(name);
        String message = "Value " + expected.toString() + " as one of " 
                + capacity 
                + " in a cache of that capacity should be retrieved";
        assertEquals(actual, expected, message);
        assertEquals(instance.createCallCount, capacity, message 
                + ", not created anew");
    }

    private static class LRUCacheImpl extends LRUCache<String, Pattern> {
        
        int createCallCount = 0;

        @Override
        protected Pattern create(String name) {
            this.createCallCount++;
            return Pattern.compile(name);
        }

        public LRUCacheImpl(int capacity) {
            super(capacity);
        }
        
    }
    
}
