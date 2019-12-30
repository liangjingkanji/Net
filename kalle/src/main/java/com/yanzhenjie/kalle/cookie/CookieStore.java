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
package com.yanzhenjie.kalle.cookie;

import java.net.HttpCookie;
import java.net.URI;
import java.util.Collections;
import java.util.List;

/**
 * Created by Zhenjie Yan on 2018/2/20.
 */
public interface CookieStore {

    CookieStore DEFAULT = new CookieStore() {
        @Override
        public List<HttpCookie> get(URI uri) {
            return Collections.emptyList();
        }

        @Override
        public void add(URI uri, HttpCookie httpCookie) {
        }

        @Override
        public void remove(HttpCookie httpCookie) {
        }

        @Override
        public void clear() {
        }
    };

    /**
     * According to url loading cookies.
     *
     * @param uri uri.
     * @return all cookies that match the rules.
     */
    List<HttpCookie> get(URI uri);

    /**
     * Save cookie for the specified url.
     *
     * @param uri    uri.
     * @param cookie cookie.
     */
    void add(URI uri, HttpCookie cookie);

    /**
     * Remove the specified cookie.
     *
     * @param cookie cookie.
     */
    void remove(HttpCookie cookie);

    /**
     * Clear the cookie.
     */
    void clear();
}