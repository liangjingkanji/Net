/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

package com.drake.net

import android.app.Application
import android.widget.Toast
import com.drake.net.error.ResponseException
import com.yanzhenjie.kalle.Kalle
import com.yanzhenjie.kalle.KalleConfig
import com.yanzhenjie.kalle.exception.*


object NetConfig {

    lateinit var host: String
    lateinit var app: Application


    var onError: Throwable.() -> Unit = {

        val message = when (this) {
            is NetworkError -> app.getString(R.string.network_error)
            is URLError -> app.getString(R.string.url_error)
            is HostError -> app.getString(R.string.host_error)
            is ConnectTimeoutError -> app.getString(R.string.connect_timeout_error)
            is ConnectException -> app.getString(R.string.connect_exception)
            is WriteException -> app.getString(R.string.write_exception)
            is ReadTimeoutError -> app.getString(R.string.read_timeout_error)
            is DownloadError -> app.getString(R.string.download_error)
            is NoCacheError -> app.getString(R.string.no_cache_error)
            is ParseError -> app.getString(R.string.parse_error)
            is ReadException -> app.getString(R.string.read_exception)
            is ResponseException -> msg
            else -> {
                printStackTrace()
                app.getString(R.string.other_error)
            }
        }

        Toast.makeText(app, message, Toast.LENGTH_SHORT).show()
    }


    fun setKalle(block: KalleConfig.Builder.() -> Unit) {
        val builder = KalleConfig.newBuilder()
        builder.block()
        Kalle.setConfig(builder.build())
    }

    // 处理错误信息
    fun onError(block: Throwable.() -> Unit) {
        onError = block
    }


}