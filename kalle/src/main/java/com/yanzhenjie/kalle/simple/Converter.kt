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
package com.yanzhenjie.kalle.simple

import com.yanzhenjie.kalle.Request
import com.yanzhenjie.kalle.Response
import java.lang.reflect.Type

/**
 * Created by Zhenjie Yan on 2018/2/12.
 */
@Suppress("UNCHECKED_CAST")
interface Converter {

    @Throws(Exception::class)
    fun <S, F> convert(
        succeed: Type,
        failed: Type,
        request: Request,
        response: Response,
        result: Result<S, F>
    )

    companion object {

        @JvmField
        val DEFAULT: Converter = object : Converter {

            override fun <S, F> convert(
                succeed: Type,
                failed: Type,
                request: Request,
                response: Response,
                result: Result<S, F>
            ) {
                if (succeed === String::class.java) {
                    result.success = response.body().string() as S
                }
            }
        }
    }
}