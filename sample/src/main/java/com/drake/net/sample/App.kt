package com.drake.net.sample

import android.app.Application
import com.drake.net.initNet
import com.drake.statelayout.StateConfig

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // 缺省页初始化
        StateConfig.apply {
            emptyLayout = R.layout.layout_empty
            loadingLayout = R.layout.layout_loading
            errorLayout = R.layout.layout_error
        }

        initNet("http://localhost.com") {
            /*converter(object : JsonConverter() {
                override fun <S> convert(succeed: Type, body: String): S? {
                    return Moshi.Builder().build().adapter<S>(succeed).fromJson(body)
                }
            })*/
        }


    }
}