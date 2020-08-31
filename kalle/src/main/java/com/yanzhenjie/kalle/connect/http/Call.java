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

import com.yanzhenjie.kalle.Kalle;
import com.yanzhenjie.kalle.Request;
import com.yanzhenjie.kalle.Response;
import com.yanzhenjie.kalle.connect.Interceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Zhenjie Yan on 2018/2/24.
 */
public class Call {

    private static final Executor EXECUTOR = Executors.newCachedThreadPool();

    private final Request mRequest;
    private ConnectInterceptor mConnectInterceptor;
    private boolean isExecuted;
    private boolean isCanceled;

    public Call(Request request) {
        this.mRequest = request;
    }

    /**
     * Execute request.
     */
    public Response execute() throws IOException {
        if (isCanceled)
            throw new CancellationException("The request has been cancelled [" + mRequest.location() + "]");
        isExecuted = true;

        List<Interceptor> interceptors = new ArrayList<>(Kalle.getConfig().getInterceptor());
        mConnectInterceptor = new ConnectInterceptor();
        interceptors.add(mConnectInterceptor);

        Chain chain = new AppChain(interceptors, 0, mRequest, this);
        try {
            return chain.proceed(mRequest);
        } catch (Exception e) {
            if (isCanceled) {
                throw new CancellationException("The request has been cancelled [" + mRequest.location() + "]");
            } else {
                throw e;
            }
        }
    }

    /**
     * The call is executed.
     *
     * @return true, otherwise is false.
     */
    public boolean isExecuted() {
        return isExecuted;
    }

    /**
     * The call is canceled.
     *
     * @return true, otherwise is false.
     */
    public boolean isCanceled() {
        return isCanceled;
    }

    /**
     * Cancel the call.
     */
    public void cancel() {
        if (!isCanceled) {
            isCanceled = true;
            if (mConnectInterceptor != null) {
                mConnectInterceptor.cancel();
            }
        }
    }

    /**
     * Cancel the call asynchronously.
     */
    public void asyncCancel() {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                cancel();
            }
        });
    }
}