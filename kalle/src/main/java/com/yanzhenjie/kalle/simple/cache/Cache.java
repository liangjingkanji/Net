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

import com.yanzhenjie.kalle.Headers;

import java.io.Serializable;

/**
 * <p>
 * CacheStore entity class.
 * </p>
 * Created in Jan 10, 2016 12:43:10 AM.
 */
public class Cache implements Serializable {

    private String mKey;
    private int mCode;
    private Headers mHeaders;
    private byte[] mBody;
    private long mExpires;

    public Cache() {
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        mCode = code;
    }

    public Headers getHeaders() {
        return mHeaders;
    }

    public void setHeaders(Headers headers) {
        mHeaders = headers;
    }

    public byte[] getBody() {
        return mBody;
    }

    public void setBody(byte[] body) {
        this.mBody = body;
    }

    public long getExpires() {
        return mExpires;
    }

    public void setExpires(long expires) {
        this.mExpires = expires;
    }
}
