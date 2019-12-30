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

import android.text.TextUtils;

import com.yanzhenjie.kalle.util.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import static com.yanzhenjie.kalle.Headers.VALUE_APPLICATION_URLENCODED;

/**
 * Created by Zhenjie Yan on 2018/2/24.
 */
public class UrlBody extends BaseContent<StringBody> implements RequestBody {

    private final Params mParams;
    private final Charset mCharset;
    private final String mContentType;
    private UrlBody(Builder builder) {
        this.mParams = builder.mParams.build();
        this.mCharset = builder.mCharset == null ? Kalle.getConfig().getCharset() : builder.mCharset;
        this.mContentType = TextUtils.isEmpty(builder.mContentType) ? VALUE_APPLICATION_URLENCODED : builder.mContentType;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Copy parameters from url body.
     */
    public Params copyParams() {
        return mParams;
    }

    @Override
    public long contentLength() {
        String body = mParams.toString(true);
        return IOUtils.toByteArray(body, mCharset).length;
    }

    @Override
    public String contentType() {
        return mContentType + "; charset=" + mCharset.name();
    }

    @Override
    protected void onWrite(OutputStream writer) throws IOException {
        String body = mParams.toString(true);
        IOUtils.write(writer, body, mCharset);
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean encode) {
        return mParams.toString(encode);
    }

    public static class Builder {

        private Charset mCharset;
        private String mContentType;
        private Params.Builder mParams;

        private Builder() {
            this.mParams = Params.newBuilder();
        }

        /**
         * Data charset.
         */
        public Builder charset(Charset charset) {
            this.mCharset = charset;
            return this;
        }

        /**
         * Content type.
         */
        public Builder contentType(String contentType) {
            this.mContentType = contentType;
            return this;
        }

        /**
         * Add parameter.
         */
        public Builder param(String key, int value) {
            mParams.add(key, value);
            return this;
        }

        /**
         * Add parameter.
         */
        public Builder param(String key, long value) {
            mParams.add(key, value);
            return this;
        }

        /**
         * Add parameter.
         */
        public Builder param(String key, boolean value) {
            mParams.add(key, value);
            return this;
        }

        /**
         * Add parameter.
         */
        public Builder param(String key, char value) {
            mParams.add(key, value);
            return this;
        }

        /**
         * Add parameter.
         */
        public Builder param(String key, double value) {
            mParams.add(key, value);
            return this;
        }

        /**
         * Add parameter.
         */
        public Builder param(String key, float value) {
            mParams.add(key, value);
            return this;
        }

        /**
         * Add parameter.
         */
        public Builder param(String key, short value) {
            mParams.add(key, value);
            return this;
        }

        /**
         * Add parameter.
         */
        public Builder param(String key, CharSequence value) {
            mParams.add(key, value);
            return this;
        }

        /**
         * Add parameter.
         */
        public Builder param(String key, String value) {
            mParams.add(key, value);
            return this;
        }

        /**
         * Add parameters.
         */
        public Builder param(String key, List<String> values) {
            mParams.add(key, values);
            return this;
        }

        /**
         * Add parameters.
         */
        public Builder params(Params params) {
            mParams.add(params);
            return this;
        }

        /**
         * Remove parameters.
         */
        public Builder removeParam(String key) {
            mParams.remove(key);
            return this;
        }

        /**
         * Clear parameters.
         */
        public Builder clearParams() {
            mParams.clear();
            return this;
        }

        public UrlBody build() {
            return new UrlBody(this);
        }
    }
}