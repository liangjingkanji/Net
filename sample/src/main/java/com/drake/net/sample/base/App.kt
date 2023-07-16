package com.drake.net.sample.base

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.drake.net.NetConfig
import com.drake.net.sample.BuildConfig

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        NetConfig.initialize("", this) {

            if (BuildConfig.DEBUG) {
                addInterceptor(
                    ChuckerInterceptor.Builder(this@App).collector(ChuckerCollector(this@App)).maxContentLength(250000L).redactHeaders(emptySet()).alwaysReadResponseBody(false).build()
                )
            }
        }
    }
}

