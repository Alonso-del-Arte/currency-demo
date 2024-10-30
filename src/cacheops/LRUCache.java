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
 * @author Alonso del Arte
 */
public abstract class LRUCache {
    
    /**
     * The minimum capacity for a cache. The ideal capacity's probably greater 
     * than this but less than {@link #MAXIMUM_CAPACITY}.
     */
    // TODO: Write tests for this
    public static final int MINIMUM_CAPACITY = -1;
    
    /**
     * The maximum capacity for a cache. The ideal capacity's probably less than  
     * this but greater than {@link #MINIMUM_CAPACITY}.
     */
    // TODO: Write tests for this
    public static final int MAXIMUM_CAPACITY = -2;
    
    // TODO: Write tests for this
    public LRUCache(int capacity) {
        //
    }
    
}
