/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.drake.net.convert

import com.drake.net.error.JsonStructureException
import com.drake.net.error.ParseJsonException
import com.drake.net.error.RequestParamsException
import com.drake.net.error.ServerResponseException
import com.yanzhenjie.kalle.Response
import com.yanzhenjie.kalle.simple.Converter
import com.yanzhenjie.kalle.simple.SimpleResponse
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type


/**
 * 默认的转换器要求数据结构为JSON对象
 */
@Suppress("UNCHECKED_CAST")
abstract class DefaultConverter(
    val successCode: String = "1",
    val codeName: String = "code",
    val msgName: String = "msg"
) : Converter {


    @Throws(Exception::class)
    override fun <S, F> convert(
        succeed: Type,
        failed: Type,
        response: Response,
        fromCache: Boolean
    ): SimpleResponse<S, F> {
        var succeedData: S? = null
        var failedData: F? = null

        val body = response.body().string()
        var code = response.code()

        when {
            code in 200..299 -> {
                try {
                    val jsonObject = JSONObject(body)
                    val responseCode = jsonObject.optString(codeName)

                    if (responseCode == successCode) {
                        if (succeed === String::class.java) {
                            succeedData = body as S
                        } else {
                            try {
                                succeedData = convert(succeed, body)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                throw ParseJsonException(body)
                            }
                        }
                    } else {
                        failedData = jsonObject.optString(msgName) as F
                        code = responseCode.toInt()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    throw JsonStructureException(body)
                }
            }
            code in 400..499 -> throw RequestParamsException()
            code >= 500 -> throw ServerResponseException()
        }

        return SimpleResponse.newBuilder<S, F>().code(code)
            .headers(response.headers())
            .fromCache(fromCache)
            .succeed(succeedData)
            .failed(failedData)
            .build()
    }


    abstract fun <S> convert(succeed: Type, body: String): S?
}
