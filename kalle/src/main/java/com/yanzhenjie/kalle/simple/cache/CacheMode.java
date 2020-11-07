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
package com.yanzhenjie.kalle.simple.cache;

/**
 * Created by Zhenjie Yan on 2018/2/18.
 */
public enum CacheMode {
    /**
     * Follow the Http standard protocol.
     */
    HTTP,
    /**
     * Follow the Http standard protocol, but it will be cached.
     */
    HTTP_YES_THEN_WRITE_CACHE,
    /**
     * Only get the results from the network.
     */
    NETWORK,
    /**
     * Just get results from the network, and then decide whether to cache according to the Http protocol.
     */
    NETWORK_YES_THEN_HTTP,
    /**
     * Only get the results from the network, but it will be cached.
     */
    NETWORK_YES_THEN_WRITE_CACHE,
    /**
     * Get results first from the network, and from the cache if the network fails.
     */
    NETWORK_NO_THEN_READ_CACHE,
    /**
     * Just get the result from the cache.
     */
    READ_CACHE,
    /**
     * First get the result from the cache, if the cache does not exist, get the result from the network.
     */
    READ_CACHE_NO_THEN_NETWORK,
    /**
     * First get the result from the cache, if the cache does not exist, get results from the network, and follow the http protocol.
     */
    READ_CACHE_NO_THEN_HTTP,
    /**
     * 本地有缓存则读取缓存，如果没有缓存则读取网络并且写入缓存
     */
    READ_CACHE_NO_THEN_NETWORK_THEN_WRITE_CACHE
}