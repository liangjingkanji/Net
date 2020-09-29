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

import com.yanzhenjie.kalle.recorder.LogRecorder;

import java.net.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created in Nov 4, 2015 8:28:50 AM.
 */
public abstract class Request {

    private final RequestMethod mMethod;
    private final Headers mHeaders;
    private final Proxy mProxy;
    private final SSLSocketFactory mSSLSocketFactory;
    private final HostnameVerifier mHostnameVerifier;
    private final int mConnectTimeout;
    private final int mReadTimeout;
    private final Object mTag;
    private final Object uid;
    private String logId = LogRecorder.INSTANCE.generateId();
    private String logRequestBody;
    private long requestTime = System.currentTimeMillis();
    private String location;

    /**
     * 请求Id, 每个请求都有独一无二的Id
     */
    public String logId() {
        return logId;
    }

    /**
     * 请求开始时间
     */
    public long getRequestStartTime() {
        return requestTime;
    }

    /**
     * 请求体
     */
    public String logRequestBody() {
        if (logRequestBody == null) {
            return copyParams().toString();
        } else return logRequestBody;
    }

    /**
     * 设置一个请求体, 默认使用copyParams()
     */
    public void logRequestBody(String logRequestBody) {
        this.logRequestBody = logRequestBody;
    }

    /**
     * 一个表示请求的地址值, 因为某些接口可能存在无法通过Url来判断请求地址(例如通过参数区分接口), 这个时候可以通过location来为开发者标识
     * 默认使用Request.url()
     */
    public void location(String location) {
        this.location = location;
    }

    /**
     * 一个表示请求的地址值
     */
    public String location() {
        if (location != null) {
            return location;
        } else return url().toString();
    }

    protected <T extends Api<T>> Request(Api<T> api) {
        this.mMethod = api.mMethod;
        this.mHeaders = api.mHeaders;

        this.mProxy = api.mProxy;
        this.mSSLSocketFactory = api.mSSLSocketFactory;
        this.mHostnameVerifier = api.mHostnameVerifier;
        this.mConnectTimeout = api.mConnectTimeout;
        this.mReadTimeout = api.mReadTimeout;
        this.mTag = api.mTag;
        this.uid = api.uid;
    }

    /**
     * Get url.
     */
    public abstract Url url();

    /**
     * Get params.
     */
    public abstract Params copyParams();

    /**
     * Get request body.
     */
    public abstract RequestBody body();

    /**
     * Get method.
     */
    public RequestMethod method() {
        return mMethod;
    }

    /**
     * Get headers.
     */
    public Headers headers() {
        return mHeaders;
    }

    /**
     * Get proxy server.
     */
    public Proxy proxy() {
        return mProxy;
    }

    /**
     * Get SSLSocketFactory.
     */
    public SSLSocketFactory sslSocketFactory() {
        return mSSLSocketFactory;
    }

    /**
     * Get the HostnameVerifier.
     */
    public HostnameVerifier hostnameVerifier() {
        return mHostnameVerifier;
    }

    /**
     * Get the connection timeout time, Unit is a millisecond.
     */
    public int connectTimeout() {
        return mConnectTimeout;
    }

    /**
     * Get the readResponse timeout time, Unit is a millisecond.
     */
    public int readTimeout() {
        return mReadTimeout;
    }

    /**
     * Get tag.
     */
    public Object tag() {
        return mTag;
    }

    public Object uid() {
        return uid;
    }

    public static abstract class Api<T extends Api<T>> {

        private final RequestMethod mMethod;
        private final Headers mHeaders = new Headers();
        private Proxy mProxy = Kalle.getConfig().getProxy();
        private SSLSocketFactory mSSLSocketFactory = Kalle.getConfig().getSSLSocketFactory();
        private HostnameVerifier mHostnameVerifier = Kalle.getConfig().getHostnameVerifier();
        private int mConnectTimeout = Kalle.getConfig().getConnectTimeout();
        private int mReadTimeout = Kalle.getConfig().getReadTimeout();
        private Object mTag;
        private Object uid;

        protected Api(RequestMethod method) {
            this.mMethod = method;
            this.mHeaders.add(Kalle.getConfig().getHeaders());
        }

        /**
         * Overlay path.
         */
        public abstract T path(int value);

        /**
         * Overlay path.
         */
        public abstract T path(long value);

        /**
         * Overlay path.
         */
        public abstract T path(boolean value);

        /**
         * Overlay path.
         */
        public abstract T path(char value);

        /**
         * Overlay path.
         */
        public abstract T path(double value);

        /**
         * Overlay path.
         */
        public abstract T path(float value);

        /**
         * Overlay path.
         */
        public abstract T path(String value);

        /**
         * Add a new header.
         */
        public T addHeader(String key, String value) {
            mHeaders.add(key, value);
            return (T) this;
        }

        /**
         * If the target key exists, replace it, if not, add.
         */
        public T setHeader(String key, String value) {
            mHeaders.set(key, value);
            return (T) this;
        }

        /**
         * Set headers.
         */
        public T setHeaders(Headers headers) {
            mHeaders.set(headers);
            return (T) this;
        }

        /**
         * Remove the key from the information.
         */
        public T removeHeader(String key) {
            mHeaders.remove(key);
            return (T) this;
        }

        /**
         * Remove all header.
         */
        public T clearHeaders() {
            mHeaders.clear();
            return (T) this;
        }

        /**
         * Add parameter.
         */
        public abstract T param(String key, int value);

        /**
         * Add parameter.
         */
        public abstract T param(String key, long value);

        /**
         * Add parameter.
         */
        public abstract T param(String key, boolean value);

        /**
         * Add parameter.
         */
        public abstract T param(String key, char value);

        /**
         * Add parameter.
         */
        public abstract T param(String key, double value);

        /**
         * Add parameter.
         */
        public abstract T param(String key, float value);

        /**
         * Add parameter.
         */
        public abstract T param(String key, short value);

        /**
         * Add parameter.
         */
        public abstract T param(String key, String value);

        /**
         * Add parameters.
         */
        public abstract T param(String key, List<String> value);

        /**
         * Remove parameters.
         */
        public abstract T removeParam(String key);

        /**
         * Clear parameters.
         */
        public abstract T clearParams();

        /**
         * Proxy information for this request.
         */
        public T proxy(Proxy proxy) {
            this.mProxy = proxy;
            return (T) this;
        }

        /**
         * SSLSocketFactory for this request.
         */
        public T sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            this.mSSLSocketFactory = sslSocketFactory;
            return (T) this;
        }

        /**
         * HostnameVerifier for this request.
         */
        public T hostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.mHostnameVerifier = hostnameVerifier;
            return (T) this;
        }

        /**
         * Connect timeout for this request.
         */
        public T connectTimeout(int timeout, TimeUnit timeUnit) {
            long time = timeUnit.toMillis(timeout);
            this.mConnectTimeout = (int) Math.min(time, Integer.MAX_VALUE);
            return (T) this;
        }

        /**
         * Read timeout for this request.
         */
        public T readTimeout(int timeout, TimeUnit timeUnit) {
            long time = timeUnit.toMillis(timeout);
            this.mReadTimeout = (int) Math.min(time, Integer.MAX_VALUE);
            return (T) this;
        }

        /**
         * Tag.
         */
        public T tag(Object tag) {
            this.mTag = tag;
            return (T) this;
        }

        /**
         * 唯一标记
         */
        public T uid(Object uid) {
            this.uid = uid;
            return (T) this;
        }
    }
}