/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：1/15/20 3:15 PM
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