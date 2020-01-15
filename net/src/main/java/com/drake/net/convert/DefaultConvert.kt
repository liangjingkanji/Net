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
        result: Result<S, F>
    ) {
        val body = response.body().string()
        val code = response.code()

        when {
            code in 200..299 -> {

                if (succeed === String::class.java) {
                    result.success = body as S
                    return
                }

                val jsonObject = JSONObject(body.parseBody())
                val responseCode = jsonObject.getString(this.code)

                if (responseCode == success) {
                    result.success = body.parseJson(succeed)
                } else {
                    result.failure =
                        ResponseException(code, jsonObject.getString(msg), request) as F
                }

            }
            code in 400..499 -> throw RequestParamsException(code, request)
            code >= 500 -> throw ServerResponseException(code, request)
        }
    }

    /**
     * 解析数据用于获取基本接口信息
     */
    fun String.parseBody(): String {
        return this
    }

    /**
     * 解析JSON数据
     *
     * @param succeed Type 请求函数传过来的字节码类型
     * @return S? 解析后的数据实体
     */
    abstract fun <S> String.parseJson(succeed: Type): S?

}
