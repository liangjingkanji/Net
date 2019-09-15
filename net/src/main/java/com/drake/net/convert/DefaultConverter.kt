/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

package com.drake.net.convert

import com.drake.net.NetConfig
import com.squareup.moshi.Moshi
import com.yanzhenjie.kalle.Response
import com.yanzhenjie.kalle.simple.Converter
import com.yanzhenjie.kalle.simple.SimpleResponse
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.lang.reflect.Type


@Suppress("UNCHECKED_CAST")
class DefaultConverter(val successCode: Int = 1) :
    Converter {


    @Throws(Exception::class)
    override fun <S, F> convert(
        succeed: Type,
        failed: Type,
        response: Response,
        fromCache: Boolean
    ): SimpleResponse<S, F> {
        var succeedData: S? = null
        var failedData: F? = null

        val json: String =
            NetConfig.listener?.parse(response.body().string()) ?: response.body().string()

        var code = response.code()

        when {
            code in 200..299 -> {
                try {
                    val jsonObject = JSONObject(json)
                    val responseCode = jsonObject.optInt("code")

                    if (responseCode == successCode) {
                        run {
                            if (succeed === String::class.java) {
                                succeedData = json as S
                            } else {
                                try {
                                    succeedData = MOSHI.adapter<S>(succeed).fromJson(json)
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                    failedData = "无法解析数据" as F
                                }
                            }
                        }
                    } else {
                        failedData = jsonObject.optString("errorMessage") as F
                        code = responseCode
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    failedData = "无法解析错误信息" as F
                }
            }
            code in 400..499 -> failedData = "发生异常错误" as F
            code >= 500 -> failedData = "服务器开小差啦" as F
        }

        return SimpleResponse.newBuilder<S, F>().code(code)
            .headers(response.headers())
            .fromCache(fromCache)
            .succeed(succeedData)
            .failed(failedData)
            .build()
    }

    companion object {

        private val MOSHI = Moshi.Builder().build()
    }
}
