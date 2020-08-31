/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.kalle.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created on 2016/6/23.
 */
public class ListMap<K, V> {

    private Map<K, List<V>> mSource;

    public ListMap(Map<K, List<V>> source) {
        this.mSource = source;
    }

    /**
     * Add key-values pair.
     *
     * @param key   key, unique.
     * @param value value, a key has multiple values.
     */
    public void add(K key, V value) {
        if (!mSource.containsKey(key))
            mSource.put(key, new ArrayList<V>(1));
        mSource.get(key).add(value);
    }

    /**
     * Add key-values pair.
     *
     * @param key    key, unique.
     * @param values values, a key has multiple values.
     */
    public void add(K key, List<V> values) {
        for (V value : values) {
            add(key, value);
        }
    }

    /**
     * Replace key-values pair,
     * if the key exists to add value, create the key if the key does not exist, add value.
     *
     * @param key   key, unique.
     * @param value value, a key has multiple values.
     */
    public void set(K key, V value) {
        mSource.remove(key);
        add(key, value);
    }

    /**
     * Replace key-values pair,
     * if the key exists to add value, create the key if the key does not exist, add value.
     *
     * @param key    key, unique.
     * @param values values, a key has multiple values.
     */
    public void set(K key, List<V> values) {
        mSource.put(key, values);
    }

    /**
     * Get all values of a key.
     *
     * @param key key.
     * @return if the key does not exist, it may be null.
     */
    public List<V> get(K key) {
        return mSource.get(key);
    }

    /**
     * Get the first value of the key.
     *
     * @param key key.
     * @return if the key does not exist, it may be null.
     */
    public V getFirst(K key) {
        List<V> values = mSource.get(key);
        if (values != null && values.size() > 0)
            return values.get(0);
        return null;
    }

    /**
     * Get {@link Set} view of the mappings.
     *
     * @return a set view of the mappings.
     * @see Map#entrySet()
     */
    public Set<Map.Entry<K, List<V>>> entrySet() {
        return mSource.entrySet();
    }

    /**
     * Get {@link Set} view of the keys.
     *
     * @return a set view of the keys.
     * @see Map#keySet()
     */
    public Set<K> keySet() {
        return mSource.keySet();
    }

    /**
     * Get the count of key-values.
     *
     * @return always greater than or equal to 0.
     * @see Map#size()
     */
    public int size() {
        return mSource.size();
    }

    /**
     * Map is empty.
     *
     * @return true if there are no key-values pairs.
     * @see Map#isEmpty()
     */
    public boolean isEmpty() {
        return mSource.isEmpty();
    }

    /**
     * Map contains the key.
     *
     * @param key key.
     * @return true if there contains the key.
     */
    public boolean containsKey(K key) {
        return mSource.containsKey(key);
    }

    /**
     * Remove the key and all values of key.
     *
     * @param key key.
     * @return all values.
     */
    public List<V> remove(K key) {
        return mSource.remove(key);
    }

    /**
     * Remove all key and all values.
     */
    public void clear() {
        mSource.clear();
    }

    /**
     * Convert to Map.
     */
    public Map<K, List<V>> toMap() {
        return mSource;
    }
}