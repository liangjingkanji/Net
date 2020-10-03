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

@file:Suppress("MemberVisibilityCanBePrivate")

package com.drake.net.convert

import com.drake.net.error.RequestParamsException
import com.drake.net.error.ResponseException
import com.drake.net.error.ServerResponseException
import com.yanzhenjie.kalle.Request
import com.yanzhenjie.kalle.Response
import com.yanzhenjie.kalle.exception.ParseError
import com.yanzhenjie.kalle.simple.Converter
import org.json.JSONObject
import java.lang.reflect.Type


/**
 * 默认的转换器实现, 如果不满足需求建议将该文件复制到项目中修改
 *
 * @param success  后端定义为成功状态的错误码值
 * @param code  错误码在JSON中的字段名
 * @param message  错误信息在JSON中的字段名
 */
@Suppress("UNCHECKED_CAST")
abstract class DefaultConvert(val success: String = "0",
                              val code: String = "code",
                              val message: String = "msg") : Converter {

    override fun <S> convert(succeed: Type,
                             request: Request,
                             response: Response,
                             cache: Boolean): S? {
        val body = response.body().string()
        response.logBody = body  // 日志记录响应信息
        val code = response.code()
        when {
            code in 200..299 -> { // 请求成功
                val jsonObject = JSONObject(body) // 获取JSON中后端定义的错误码和错误信息
                if (jsonObject.getString(this.code) == success) { // 对比后端自定义错误码
                    return if (succeed === String::class.java) body as S else body.parseBody(succeed)
                } else { // 错误码匹配失败, 开始写入错误异常
                    throw ResponseException(code, jsonObject.getString(message), request)
                }
            }
            code in 400..499 -> throw RequestParamsException(code, request) // 请求参数错误
            code >= 500 -> throw ServerResponseException(code, request) // 服务器异常错误
            else -> throw ParseError(request)
        }
    }

    /**
     * 解析字符串数据
     * 一般用于解析JSON
     * @param succeed 请求函数定义的泛型, 例如一般的Moshi/Gson解析数据需要使用
     * @receiver 原始字符串
     */
    abstract fun <S> String.parseBody(succeed: Type): S?
}
