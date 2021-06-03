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

@file:Suppress("MemberVisibilityCanBePrivate", "UNCHECKED_CAST")

package com.drake.net.convert

import com.drake.net.NetConfig
import com.drake.net.exception.ConvertException
import com.drake.net.exception.RequestParamsException
import com.drake.net.exception.ResponseException
import com.drake.net.exception.ServerResponseException
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type


/**
 * 常见的JSON转换器实现, 如果不满意继承实现自定义的业务逻辑
 *
 * @param success  后端定义为成功状态的错误码值
 * @param code  错误码在JSON中的字段名
 * @param message  错误信息在JSON中的字段名
 */
abstract class JSONConvert(
    val success: String = "0",
    val code: String = "code",
    val message: String = "msg"
) : NetConverter {

    override fun <R> onConvert(succeed: Type, response: Response): R? {
        try {
            return NetConverter.DEFAULT.onConvert<R>(succeed, response)
        } catch (e: ConvertException) {
            val code = response.code
            when {
                code in 200..299 -> { // 请求成功
                    val bodyString = response.body?.string() ?: return null
                    return try {
                        val json = JSONObject(bodyString) // 获取JSON中后端定义的错误码和错误信息
                        if (json.getString(this.code) == success) { // 对比后端自定义错误码
                            bodyString.parseBody<R>(succeed)
                        } else { // 错误码匹配失败, 开始写入错误异常
                            val errorMessage = json.optString(
                                message,
                                NetConfig.app.getString(com.drake.net.R.string.no_error_message)
                            )
                            throw ResponseException(response, errorMessage)
                        }
                    } catch (e: JSONException) { // 固定格式JSON分析失败直接解析JSON
                        bodyString.parseBody<R>(succeed)
                    }
                }
                code in 400..499 -> throw RequestParamsException(response) // 请求参数错误
                code >= 500 -> throw ServerResponseException(response) // 服务器异常错误
                else -> throw ConvertException(response)
            }
        }
    }

    /**
     * 反序列化JSON
     *
     * @param succeed JSON对象的类型
     * @receiver 原始字符串
     */
    abstract fun <R> String.parseBody(succeed: Type): R?
}
