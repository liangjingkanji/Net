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

import java.io.IOException;

/**
 * Created by Zhenjie Yan on 2018/3/6.
 */
public interface Chain {
    /**
     * Get the request in the chain.
     *
     * @return target request.
     */
    Request request();

    /**
     * Proceed to the next request processing.
     *
     * @param request target request.
     * @return {@link Response}.
     * @throws IOException io exception may occur during the implementation, you can handle or throw.
     */
    Response proceed(Request request) throws IOException;

    /**
     * Return {@link Call}, request will be executed on it.
     *
     * @deprecated use {@link #call()} instead.
     */
    @Deprecated
    Call newCall();

    /**
     * Return {@link Call}, request will be executed on it.
     *
     * @return {@link Call}.
     */
    Call call();
}