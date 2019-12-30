/*
 * Copyright © 2018 Zhenjie Yan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.kalle.simple.cache;

/**
 * <p>
 * CacheStore interface.
 * </p>
 * Created in Dec 14, 2015 5:52:41 PM.
 */
public interface CacheStore {

    CacheStore DEFAULT = new CacheStore() {
        @Override
        public Cache get(String key) {
            return null;
        }

        @Override
        public boolean replace(String key, Cache cache) {
            return true;
        }

        @Override
        public boolean remove(String key) {
            return true;
        }

        @Override
        public boolean clear() {
            return true;
        }
    };

    /**
     * Get the cache.
     *
     * @param key unique key.
     * @return cache.
     */
    Cache get(String key);

    /**
     * Save or set the cache.
     *
     * @param key   unique key.
     * @param cache cache.
     * @return cache.
     */
    boolean replace(String key, Cache cache);

    /**
     * Remove cache.
     *
     * @param key unique.
     * @return cache.
     */
    boolean remove(String key);

    /**
     * Clear all data.
     *
     * @return returns true if successful, false otherwise.
     */
    boolean clear();
}