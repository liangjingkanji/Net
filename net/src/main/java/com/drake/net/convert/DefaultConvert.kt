/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.drake.net.convert

import com.drake.net.error.RequestParamsException
import com.drake.net.error.ResponseException
import com.drake.net.error.ServerResponseException
import com.yanzhenjie.kalle.Request
import com.yanzhenjie.kalle.Response
import com.yanzhenjie.kalle.simple.Converter
import com.yanzhenjie.kalle.simple.Result
import org.json.JSONObject
import java.lang.reflect.Type


/**
 * 默认的转换器实现, 如果不满足需求建议将该文件复制到项目中修改
 *
 * @param success  后端定义为成功状态的错误码值
 * @param code  错误码在Json中的字段名
 * @param message  错误信息在Json中的字段名
 */
@Suppress("UNCHECKED_CAST")
abstract class DefaultConvert(
    val success: String = "0",
    val code: String = "code",
    val message: String = "msg"
) : Converter {

    override fun <S, F> convert(
            succeed: Type,
            failed: Type,
            request: Request,
            response: Response,
            result: Result<S, F>
    ) {
        val body = response.body().string()
        val code = response.code()

        when {
            code in 200..299 -> {

                val jsonObject = JSONObject(body)

                if (jsonObject.getString(this.code) == success) {
                    result.success = if (succeed === String::class.java) body as S else body.parseBody(succeed)
                } else {
                    result.failure = ResponseException(code, jsonObject.getString(message), request) as F
                }
            }
            code in 400..499 -> throw RequestParamsException(code, request)
            code >= 500 -> throw ServerResponseException(code, request)
        }
    }

    abstract fun <S> String.parseBody(succeed: Type): S?

}
