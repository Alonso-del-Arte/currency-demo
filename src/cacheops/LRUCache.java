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
    
    private V mostRecentlyCreated = null;
    
    /**
     * Creates a value for a given name. Ideally this function should only be 
     * called by {@link #retrieve(java.lang.Object) retrieve()}.
     * @param name The name to create a value for. Once the value is in the 
     * cache, this name can be used to retrieve it.
     * @return A new value. Preferably not null.
     */
    protected abstract V create(N name);
    
    // TODO: Write tests for this
    boolean has(V value) {
        return value.equals(this.mostRecentlyCreated);
    }

    // TODO: Write tests for this
    public V retrieve(N name) {
        this.mostRecentlyCreated = this.create(name);
        return this.mostRecentlyCreated;
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
    }
    
}
