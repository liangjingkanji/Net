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
package com.yanzhenjie.kalle.simple;

import com.yanzhenjie.kalle.NetCancel;

import java.lang.reflect.Type;

/**
 * Created by Zhenjie Yan on 2018/2/13.
 */
public class RequestManager {

    private static RequestManager sInstance;

    private RequestManager() {
    }

    public static RequestManager getInstance() {
        if (sInstance == null) {
            synchronized (RequestManager.class) {
                if (sInstance == null) {
                    sInstance = new RequestManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * Execute a request.
     *
     * @param request request.
     * @param succeed the data type when the business succeed.
     * @param <S>     target object parameter.
     * @return the response to this request.
     */
    public <S> S perform(SimpleUrlRequest request, Type succeed) throws Exception {
        UrlWorker<S> worker = new UrlWorker<>(request, succeed);
        NetCancel.INSTANCE.add(request.uid(), worker);
        return worker.call();
    }

    /**
     * Execute a request.
     *
     * @param request request.
     * @param succeed the data type when the business succeed.
     * @param <S>     target object parameter.
     * @return the response to this request.
     */
    public <S> S perform(SimpleBodyRequest request, Type succeed) throws Exception {
        BodyWorker<S> worker = new BodyWorker<>(request, succeed);
        NetCancel.INSTANCE.add(request.uid(), worker);
        return worker.call();
    }
}