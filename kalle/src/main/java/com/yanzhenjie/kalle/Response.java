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
package com.yanzhenjie.kalle;

import com.yanzhenjie.kalle.util.IOUtils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created in Oct 15, 2015 8:55:37 PM.
 */
public final class Response implements Closeable {

    private final int mCode;
    private final Headers mHeaders;
    private final ResponseBody mBody;

    private Response(Builder builder) {
        this.mCode = builder.mCode;
        this.mHeaders = builder.mHeaders;
        this.mBody = builder.mBody;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    /**
     * Get the mCode of response.
     */
    public int code() {
        return mCode;
    }

    /**
     * Get http headers.
     */
    public Headers headers() {
        return mHeaders;
    }

    /**
     * Get http body.
     */
    public ResponseBody body() {
        return mBody;
    }

    @Override
    public void close() throws IOException {
        IOUtils.closeQuietly(mBody);
    }

    /**
     * It is a redirect response code.
     */
    public boolean isRedirect() {
        switch (mCode) {
            case 300:
            case 301:
            case 302:
            case 303:
            case 307:
            case 308:
                return true;
            case 304:
            case 305:
            case 306:
            default:
                return false;
        }
    }

    public static final class Builder {
        private int mCode;
        private Headers mHeaders;
        private ResponseBody mBody;

        public Builder() {
        }

        public Builder code(int code) {
            this.mCode = code;
            return this;
        }

        public Builder headers(Headers headers) {
            this.mHeaders = headers;
            return this;
        }

        public Builder body(ResponseBody body) {
            this.mBody = body;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }
}