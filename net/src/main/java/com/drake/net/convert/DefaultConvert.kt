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
import com.yanzhenjie.kalle.simple.SimpleResponse
import org.json.JSONObject
import java.lang.reflect.Type


/**
 * 默认的转换器实现, 如果不满足需求建议将该文件复制到项目中修改
 *
 * @property success String 错误码表示请求成功的值
 * @property code String 错误码的Key名称
 * @property msg String 错误信息的Key名称
 */
@Suppress("UNCHECKED_CAST")
abstract class DefaultConvert(
    val success: String = "0",
    val code: String = "code",
    val msg: String = "msg"
) : Converter {


    @Throws(Exception::class)
    override fun <S, F> convert(
        succeed: Type,
        failed: Type,
        request: Request,
        response: Response,
        fromCache: Boolean
    ): SimpleResponse<S, F> {
        var succeedData: S? = null
        var failedData: F? = null

        val body = response.body().string()
        var code = response.code()

        when {
            code in 200..299 -> {
                val jsonObject = JSONObject(body)
                val responseCode = jsonObject.getString(this.code)

                if (responseCode == success) {
                    succeedData = if (succeed === String::class.java) body as S
                    else convert(succeed, body)
                } else {
                    failedData = ResponseException(code, jsonObject.getString(msg), request) as F
                    code = responseCode.toInt()
                }
            }
            code in 400..499 -> throw RequestParamsException(code, request)
            code >= 500 -> throw ServerResponseException(code, request)
        }

        return SimpleResponse.newBuilder<S, F>().code(code)
            .headers(response.headers())
            .fromCache(fromCache)
            .succeed(succeedData)
            .failed(failedData)
            .build()
    }


    /**
     * 解析JSON数据
     * @param succeed Type 请求函数传过来的字节码类型
     * @param body String 一般为返回JSON
     * @return S? 解析后的数据实体
     */
    abstract fun <S> convert(succeed: Type, body: String): S?
}
