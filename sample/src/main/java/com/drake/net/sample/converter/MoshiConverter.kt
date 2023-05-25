package com.drake.net.sample.converter

import com.drake.net.convert.JSONConvert
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.json.JSONObject
import java.lang.reflect.Type

class MoshiConverter : JSONConvert(code = "errorCode", message = "errorMsg", success = "0") {

    companion object {
        private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    override fun <R> String.parseBody(succeed: Type): R? {
        val string = try {
            JSONObject(this).getString("data")
        } catch (e: Exception) {
            this
        }
        return moshi.adapter<R>(succeed).fromJson(string)
    }
}