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
package com.yanzhenjie.kalle.simple;

import com.yanzhenjie.kalle.Headers;
import com.yanzhenjie.kalle.Url;
import com.yanzhenjie.kalle.simple.cache.CacheMode;

/**
 * Created by Zhenjie Yan on 2018/2/20.
 */
public interface SimpleRequest {
    /**
     * Get the file download address.
     */
    Url url();

    /**
     * Get headers.
     */
    Headers headers();

    /**
     * Get cache mode.
     */
    CacheMode cacheMode();

    /**
     * Get cache key.
     */
    String cacheKey();

    /**
     * Get converter.
     */
    Converter converter();
}