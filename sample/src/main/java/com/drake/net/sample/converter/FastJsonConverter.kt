package com.drake.net.sample.converter

import com.alibaba.fastjson.JSON
import com.drake.net.convert.JSONConvert
import org.json.JSONObject
import java.lang.reflect.Type

class FastJsonConverter : JSONConvert(code = "errorCode", message = "errorMsg", success = "0") {

    override fun <R> String.parseBody(succeed: Type): R? {
        val string = try {
            JSONObject(this).getString("data")
        } catch (e: Exception) {
            this
        }
        return JSON.parseObject(string, succeed)
    }
}