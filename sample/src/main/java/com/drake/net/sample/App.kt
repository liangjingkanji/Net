package com.drake.net.sample

import android.app.Application
import com.drake.net.initNet

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initNet("http://localhost.com") {
            /*converter(object : JsonConverter() {
                override fun <S> convert(succeed: Type, body: String): S? {
                    return Moshi.Builder().build().adapter<S>(succeed).fromJson(body)
                }
            })*/
        }

    }
}