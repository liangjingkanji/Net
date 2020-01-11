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

import com.yanzhenjie.kalle.Request;
import com.yanzhenjie.kalle.Response;

import java.lang.reflect.Type;

/**
 * Created by Zhenjie Yan on 2018/2/12.
 */
public interface Converter {

    /**
     * Default converter.
     */
    Converter DEFAULT = new Converter() {
        @Override
        public <S, F> SimpleResponse<S, F> convert(Type succeed, Type failed, Request request, Response response, boolean fromCache) throws Exception {
            S succeedData = null;

            if (succeed == String.class) succeedData = (S) response.body().string();

            return SimpleResponse.<S, F>newBuilder()
                    .code(response.code())
                    .headers(response.headers())
                    .fromCache(fromCache)
                    .succeed(succeedData)
                    .build();
        }
    };

    /**
     * Convert data to the result of the target type.
     *
     * @param succeed   the data type when the business succeed.
     * @param failed    the data type when the business failed.
     * @param response  response of request.
     * @param fromCache the response is from the cache.
     * @param <S>       the data type.
     * @param <F>       the data type.
     * @return {@link SimpleResponse}
     * @throws Exception to prevent accidents.
     */
    <S, F> SimpleResponse<S, F> convert(Type succeed, Type failed, Request request, Response response, boolean fromCache) throws Exception;
}