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

/**
 * A least recently used (LRU) cache. The idea is that the cache makes the most 
 * recently used items available. The cache has a capacity specified at the time 
 * of construction. New items can still be added when capacity is reached, the 
 * cache simply discards the least recently used item.
 * <p>One way to implement the cache is with an array. New items are added at 
 * the first index of the array and the other items are pushed back one index. 
 * Whatever was at the last index is simply discarded.</p>
 * <p>As long as an item is in the cache, it can't be collected by the garbage 
 * collector. Once it's out of the cache, there might be no more references to 
 * the object, in which case the memory it takes up can be reclaimed.</p>
 * <p>This class is modeled on the {@code sun.misc.LRUCache<N, V>} class that 
 * {@code java.util.Scanner} uses in some implementations of the Java 
 * Development Kit (JDK). But unlike that one, this one uses no "native 
 * methods," and, perhaps more importantly, is not proprietary.</p>
 * @param <N> The type of the names for the values to cache. Ideally this should 
 * be an immutable class that is easily calculated anew, and which has {@code 
 * equals()} overridden. For example, {@code java.lang.String} is a good choice 
 * of {@code N} type.
 * @param <V> The type of the values to cache. To be worth caching, the values 
 * should be too expensive to recalculate each and every time they're needed, 
 * such as that it requires a database call or an online API call, so that it's 
 * easier to retrieve from the cache. For example, {@code 
 * java.util.regex.Pattern} is a good choice of {@code V} type.
 * @author Alonso del Arte
 */
public abstract class LRUCache<N, V> {
    
    /**
     * The minimum capacity for a cache. The ideal capacity's probably greater 
     * than this but less than {@link #MAXIMUM_CAPACITY}.
     */
    public static final int MINIMUM_CAPACITY = 4;
    
    /**
     * The maximum capacity for a cache. The ideal capacity's probably less than  
     * this but greater than {@link #MINIMUM_CAPACITY}.
     */
    public static final int MAXIMUM_CAPACITY = 128;
    
    private final int cacheCapacity;
    
    protected final Object[] names, values;
    
    private int index = 0;
    
    /**
     * Creates a value for a given name. Ideally this function should only be 
     * called by {@link #retrieve(java.lang.Object) retrieve()} or {@link 
     * #refresh(java.lang.Object) refresh()}.
     * @param name The name to create a value for. Once the value is in the 
     * cache, this name can be used to retrieve it.
     * @return A new value. Preferably not null.
     */
    protected abstract V create(N name);
    
    private static int indexOf(Object obj, Object[] array) {
        boolean notFound = true;
        int i = 0;
        while (notFound && i < array.length) {
            notFound = !obj.equals(array[i]);
            i++;
        }
        return notFound ? -1 : i - 1;
    }
    
    /**
     * Determines whether this cache has a particular value. This function is 
     * intended for internal use and testing only.
     * @param value The value to check the cache for. For example, in a cache of 
     * regular expressions, the regular expression for e-mail addresses.
     * @return True if the cache has the value, false otherwise.
     */
    boolean has(V value) {
        return indexOf(value, this.values) > -1;
    }
    
    private static void backShift(Object[] array, int shiftEndIndex) {
        for (int i = shiftEndIndex; i > 0; i--) {
            array[i] = array[i - 1];
        }
    }

    /**
     * Retrieves a value from the cache by its name, or creates it anew and adds 
     * it to the cache if it wasn't already stored. In either case, the cache 
     * notes the value is the most recently used. If a value is added to the 
     * cache and the cache was already at capacity, the least recently used 
     * value will be removed from the cache. If the name of a removed value is 
     * called for later, it will have to be created anew.
     * @param name The name for the value.
     * @return The value.
     * @throws NullPointerException If {@code name} is null. The exception 
     * message will probably be empty, and thus not very helpful.
     */
    public V retrieve(N name) {
        int ind = indexOf(name, this.names);
        V value;
        int shiftEndIndex;
        if (ind < 0) {
            value = this.create(name);
            shiftEndIndex = this.index;
            this.index++;
            if (this.index == this.cacheCapacity) {
                this.index--;
            }
        } else {
            shiftEndIndex = ind;
            value = (V) this.values[ind];
        }
        backShift(this.names, shiftEndIndex);
        backShift(this.values, shiftEndIndex);
        this.names[0] = name;
        this.values[0] = value;
        return value;
    }
    
    /**
     * Refreshes the value for a given name in the cache. If the name is not in 
     * the cache, nothing happens. Unless overridden, the behavior of this 
     * procedure is to refresh in place, so the name-value pair is not marked as 
     * more or less recent than before.
     * @param name The name for which to refresh the value. For example, in a 
     * cache of stock quotes, the stock symbol for IBM.
     */
    protected void refresh(N name) {
        int ind = indexOf(name, this.names);
        if (ind > -1) {
            this.values[ind] = this.create(name);
        }
    }
    
    /**
     * Constructor.
     * @param capacity How much capacity the cache should have.
     * @throws IllegalArgumentException If {@code capacity} is less than {@link 
     * #MINIMUM_CAPACITY} or more than {@link #MAXIMUM_CAPACITY}.
     */
    public LRUCache(int capacity) {
        if (capacity < MINIMUM_CAPACITY || capacity > MAXIMUM_CAPACITY) {
            String excMsg = "Capacity " + capacity + " is not valid";
            throw new IllegalArgumentException(excMsg);
        }
        this.cacheCapacity = capacity;
        this.names = new Object[this.cacheCapacity];
        this.values = new Object[this.cacheCapacity];
    }
    
}
