/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

package com.drake.net

import android.app.Application
import com.drake.net.convert.DefaultConverter
import com.drake.net.listener.DefaultInterceptor
import com.drake.net.listener.NetListener
import com.yanzhenjie.kalle.Kalle
import com.yanzhenjie.kalle.KalleConfig


object NetConfig {

    lateinit var host: String
    lateinit var app: Application
    var listener: NetListener? = null

    fun set(block: KalleConfig.Builder.() -> Unit) {
        val builder = KalleConfig.newBuilder()
        builder.block()
        Kalle.setConfig(builder.build())
    }
}


fun Application.setDefaultNetConfig(host: String, successCode: Int = 1) {
    NetConfig.app = this
    NetConfig.host = host

    NetConfig.set {
        addInterceptor(DefaultInterceptor())
        converter(DefaultConverter(successCode))
    }

}