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
package com.yanzhenjie.kalle;

import java.util.List;

/**
 * Created by Zhenjie Yan on 2018/2/13.
 */
public class UrlRequest extends Request {

    private final Url mUrl;

    protected UrlRequest(Api api) {
        super(api);
        this.mUrl = api.mUrl.build();
    }

    public static UrlRequest.Builder newBuilder(String url, RequestMethod method) {
        return newBuilder(Url.newBuilder(url).build(), method);
    }

    public static UrlRequest.Builder newBuilder(Url url, RequestMethod method) {
        return new UrlRequest.Builder(url, method);
    }

    /**
     * @deprecated use {@link #newBuilder(Url, RequestMethod)} instead.
     */
    @Deprecated
    public static UrlRequest.Builder newBuilder(Url.Builder url, RequestMethod method) {
        return newBuilder(url.build(), method);
    }

    @Override
    public Url url() {
        return mUrl;
    }

    @Override
    public Params copyParams() {
        return mUrl.getParams();
    }

    @Override
    public RequestBody body() {
        throw new AssertionError("It should not be called.");
    }

    public static class Api<T extends Api<T>> extends Request.Api<T> {

        private Url.Builder mUrl;

        protected Api(Url url, RequestMethod method) {
            super(method);
            this.mUrl = url.builder();
            this.mUrl.addQuery(Kalle.getConfig().getParams());
        }

        @Override
        public T path(int value) {
            mUrl.addPath(value);
            return (T) this;
        }

        @Override
        public T path(long value) {
            mUrl.addPath(value);
            return (T) this;
        }

        @Override
        public T path(boolean value) {
            mUrl.addPath(value);
            return (T) this;
        }

        @Override
        public T path(char value) {
            mUrl.addPath(value);
            return (T) this;
        }

        @Override
        public T path(double value) {
            mUrl.addPath(value);
            return (T) this;
        }

        @Override
        public T path(float value) {
            mUrl.addPath(value);
            return (T) this;
        }

        @Override
        public T path(String value) {
            mUrl.addPath(value);
            return (T) this;
        }

        @Override
        public T param(String key, int value) {
            mUrl.addQuery(key, value);
            return (T) this;
        }

        @Override
        public T param(String key, long value) {
            mUrl.addQuery(key, value);
            return (T) this;
        }

        @Override
        public T param(String key, boolean value) {
            mUrl.addQuery(key, value);
            return (T) this;
        }

        @Override
        public T param(String key, char value) {
            mUrl.addQuery(key, value);
            return (T) this;
        }

        @Override
        public T param(String key, double value) {
            mUrl.addQuery(key, value);
            return (T) this;
        }

        @Override
        public T param(String key, float value) {
            mUrl.addQuery(key, value);
            return (T) this;
        }

        @Override
        public T param(String key, short value) {
            mUrl.addQuery(key, value);
            return (T) this;
        }

        @Override
        public T param(String key, String value) {
            mUrl.addQuery(key, value);
            return (T) this;
        }

        @Override
        public T param(String key, List<String> values) {
            mUrl.addQuery(key, values);
            return (T) this;
        }

        /**
         * Add parameters to url.
         */
        public T params(Params params) {
            mUrl.addQuery(params);
            return (T) this;
        }

        /**
         * Set parameters to url.
         */
        public T setParams(Params params) {
            mUrl.setQuery(params);
            return (T) this;
        }

        @Override
        public T removeParam(String key) {
            mUrl.removeQuery(key);
            return (T) this;
        }

        @Override
        public T clearParams() {
            mUrl.clearQuery();
            return (T) this;
        }
    }

    public static class Builder extends UrlRequest.Api<UrlRequest.Builder> {

        private Builder(Url url, RequestMethod method) {
            super(url, method);
        }

        public UrlRequest build() {
            return new UrlRequest(this);
        }
    }
}