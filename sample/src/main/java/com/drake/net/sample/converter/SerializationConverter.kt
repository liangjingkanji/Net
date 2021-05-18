@file:Suppress("UNCHECKED_CAST")

package com.drake.net.sample.converter

import com.drake.net.NetConfig
import com.drake.net.convert.NetConverter
import com.drake.net.exception.ConvertException
import com.drake.net.exception.RequestParamsException
import com.drake.net.exception.ResponseException
import com.drake.net.exception.ServerResponseException
import com.drake.net.request.kType
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.Response
import org.json.JSONObject
import java.lang.reflect.Type

class SerializationConverter(
    val success: String = "0",
    val code: String = "code",
    val message: String = "msg"
) : NetConverter {

    val jsonDecoder = Json {
        ignoreUnknownKeys = true // JSON和数据模型字段可以不匹配
        coerceInputValues = true // 如果JSON字段是Null则使用默认值
    }

    override fun <R> onConvert(succeed: Type, response: Response): R? {
        try {
            return NetConverter.DEFAULT.onConvert<R>(succeed, response)
        } catch (e: ConvertException) {

            val code = response.code
            when {
                code in 200..299 -> { // 请求成功
                    val body = response.body?.string() ?: return null
                    if (succeed === String::class.java) return body as R
                    val jsonObject = JSONObject(body) // 获取JSON中后端定义的错误码和错误信息
                    if (jsonObject.getString(this.code) == success) { // 对比后端自定义错误码
                        return run {
                            val kType = response.request.kType() ?: return null
                            try {
                                jsonDecoder.decodeFromString(Json.serializersModule.serializer(kType), jsonObject.getString("data")) as R
                            } catch (e: SerializationException) {
                                throw ConvertException(response, cause = e)
                            }
                        }
                    } else { // 错误码匹配失败, 开始写入错误异常
                        throw ResponseException(response, jsonObject.optString(message, NetConfig.app.getString(com.drake.net.R.string.no_error_message)))
                    }
                }
                code in 400..499 -> throw RequestParamsException(response) // 请求参数错误
                code >= 500 -> throw ServerResponseException(response) // 服务器异常错误
                else -> throw ConvertException(response)
            }
        }
    }
}