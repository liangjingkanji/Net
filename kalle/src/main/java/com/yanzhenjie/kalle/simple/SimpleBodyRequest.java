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

import android.text.TextUtils;

import com.yanzhenjie.kalle.BodyRequest;
import com.yanzhenjie.kalle.Canceller;
import com.yanzhenjie.kalle.RequestMethod;
import com.yanzhenjie.kalle.Url;
import com.yanzhenjie.kalle.simple.cache.CacheMode;

import java.lang.reflect.Type;

import static com.yanzhenjie.kalle.simple.cache.CacheMode.HTTP;

/**
 * Created by Zhenjie Yan on 2018/2/13.
 */
public class SimpleBodyRequest extends BodyRequest implements SimpleRequest {

    private final CacheMode mCacheMode;
    private final String mCacheKey;
    private final Converter mConverter;

    private SimpleBodyRequest(Api api) {
        super(api);
        this.mCacheMode = api.mCacheMode == null ? HTTP : api.mCacheMode;
        this.mCacheKey = TextUtils.isEmpty(api.mCacheKey) ? url().toString() : api.mCacheKey;

        this.mConverter = api.mConverter;
    }

    public static SimpleBodyRequest.Api newApi(Url url, RequestMethod method) {
        return new SimpleBodyRequest.Api(url, method);
    }

    @Override
    public CacheMode cacheMode() {
        return mCacheMode;
    }

    @Override
    public String cacheKey() {
        return mCacheKey;
    }

    @Override
    public Converter converter() {
        return mConverter;
    }

    public static class Api extends BodyRequest.Api<Api> {

        private CacheMode mCacheMode;
        private String mCacheKey;

        private Converter mConverter;

        private Api(Url url, RequestMethod method) {
            super(url, method);
        }

        public Api cacheMode(CacheMode cacheMode) {
            this.mCacheMode = cacheMode;
            return this;
        }

        public Api cacheKey(String cacheKey) {
            this.mCacheKey = cacheKey;
            return this;
        }

        public Api converter(Converter converter) {
            this.mConverter = converter;
            return this;
        }

        public <S, F> SimpleResponse<S, F> perform(Type succeed, Type failed) throws Exception {
            return RequestManager.getInstance().perform(new SimpleBodyRequest(this), succeed, failed);
        }

        public <S, F> Canceller perform(Callback<S, F> callback) {
            return RequestManager.getInstance().perform(new SimpleBodyRequest(this), callback);
        }
    }
}