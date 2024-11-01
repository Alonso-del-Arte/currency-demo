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
 * A least recently used (LRU) cache.
 * @param <N> The type of the names for the values to cache. Ideally this should 
 * be an immutable class that is easily calculated anew.
 * @param <V> The type of the values to cache. To be worth caching, the values 
 * should be too expensive to recalculate each and every time they're needed, so 
 * that it's easier to retrieve from the cache.
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
    
    private final Object[] names, values;
    
    private int index = 0;
    
    /**
     * Creates a value for a given name. Ideally this function should only be 
     * called by {@link #retrieve(java.lang.Object) retrieve()}.
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

    // TODO: Write tests for this
    public V retrieve(N name) {
        int ind = indexOf(name, this.names);
        V value;
        if (ind < 0) {
            value = this.create(name);
            this.values[this.index] = value;
            this.names[this.index] = name;
            this.index++;
            if (this.index == this.cacheCapacity) {
                this.index--;
            }
        } else {
            value = (V) this.values[ind];
        }
        return value;
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
