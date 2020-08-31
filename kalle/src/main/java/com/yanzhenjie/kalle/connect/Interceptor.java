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
package com.yanzhenjie.kalle.connect;

import com.yanzhenjie.kalle.Response;
import com.yanzhenjie.kalle.connect.http.Chain;

import java.io.IOException;

/**
 * <p>
 * Intercept before call request.
 * </p>
 * Created by Zhenjie Yan on 2018/2/11.
 */
public interface Interceptor {

    /**
     * When intercepting the {@link Chain},
     * here can do some signature and login operation,
     * it should send a response and return.
     *
     * @param chain request chain.
     * @return the server connection.
     * @throws IOException io exception may occur during the implementation, you can handle or throw.
     */
    Response intercept(Chain chain) throws IOException;
}