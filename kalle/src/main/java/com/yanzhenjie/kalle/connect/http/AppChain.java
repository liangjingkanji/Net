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
package com.yanzhenjie.kalle.connect.http;

import com.yanzhenjie.kalle.Request;
import com.yanzhenjie.kalle.Response;
import com.yanzhenjie.kalle.connect.Interceptor;

import java.io.IOException;
import java.util.List;

/**
 * Created by Zhenjie Yan on 2018/3/6.
 */
class AppChain implements Chain {

    private final List<Interceptor> mInterceptors;
    private final int mTargetIndex;
    private final Request mRequest;
    private Call mCall;

    AppChain(List<Interceptor> interceptors, int targetIndex, Request request, Call call) {
        this.mInterceptors = interceptors;
        this.mTargetIndex = targetIndex;
        this.mRequest = request;
        this.mCall = call;
    }

    @Override
    public Request request() {
        return mRequest;
    }

    @Override
    public Response proceed(Request request) throws IOException {
        Interceptor interceptor = mInterceptors.get(mTargetIndex);
        Chain chain = new AppChain(mInterceptors, mTargetIndex + 1, request, mCall);
        return interceptor.intercept(chain);
    }

    @Override
    public Call newCall() {
        return mCall;
    }

    @Override
    public Call call() {
        return mCall;
    }
}