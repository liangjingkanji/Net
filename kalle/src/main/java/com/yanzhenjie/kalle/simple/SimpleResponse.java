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

/**
 * Created in Oct 15, 2015 8:55:37 PM.
 */
public final class SimpleResponse<Succeed, Failed> {

    private final int mCode;
    private final Headers mHeaders;
    private final boolean mFromCache;
    private final Succeed mSucceed;
    private final Failed mFailed;
    private SimpleResponse(Builder<Succeed, Failed> builder) {
        this.mCode = builder.mCode;
        this.mHeaders = builder.mHeaders;
        this.mFromCache = builder.mFromCache;

        this.mSucceed = builder.mSucceed;
        this.mFailed = builder.mFailed;
    }

    public static <Succeed, Failed> Builder<Succeed, Failed> newBuilder() {
        return new Builder<>();
    }

    /**
     * Get the headers code of handle.
     */
    public int code() {
        return mCode;
    }

    /**
     * Get http headers headers.
     */
    public Headers headers() {
        return mHeaders;
    }

    /**
     * Whether the data returned from the cache.
     *
     * @return true: the data from cache, false: the data from network.
     */
    public boolean fromCache() {
        return mFromCache;
    }

    /**
     * Business successful.
     */
    public boolean isSucceed() {
        return mFailed == null || mSucceed != null;
    }

    /**
     * Get business success data.
     */
    public Succeed succeed() {
        return mSucceed;
    }

    /**
     * Get business failure data.
     */
    public Failed failed() {
        return mFailed;
    }

    public static final class Builder<Succeed, Failed> {
        private int mCode;
        private Headers mHeaders;
        private boolean mFromCache;

        private Failed mFailed;
        private Succeed mSucceed;

        private Builder() {
        }

        public Builder<Succeed, Failed> code(int code) {
            this.mCode = code;
            return this;
        }

        public Builder<Succeed, Failed> headers(Headers headers) {
            this.mHeaders = headers;
            return this;
        }

        public Builder<Succeed, Failed> fromCache(boolean fromCache) {
            this.mFromCache = fromCache;
            return this;
        }

        public Builder<Succeed, Failed> succeed(Succeed succeed) {
            this.mSucceed = succeed;
            return this;
        }

        public Builder<Succeed, Failed> failed(Failed failed) {
            this.mFailed = failed;
            return this;
        }

        public SimpleResponse<Succeed, Failed> build() {
            return new SimpleResponse<>(this);
        }
    }
}