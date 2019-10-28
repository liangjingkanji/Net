/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

@file:Suppress("unused")

package com.drake.net

import android.app.Application
import android.app.Dialog
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.drake.brv.PageRefreshLayout
import com.drake.net.error.ParseJsonException
import com.drake.net.error.RequestParamsException
import com.drake.net.error.ResponseException
import com.drake.net.error.ServerResponseException
import com.drake.net.observer.DialogObserver
import com.drake.net.observer.PageObserver
import com.yanzhenjie.kalle.Kalle
import com.yanzhenjie.kalle.KalleConfig
import com.yanzhenjie.kalle.exception.*


object NetConfig {

    lateinit var host: String
    lateinit var app: Application

    internal var onError: Throwable.() -> Unit = {

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
            is ReadException -> app.getString(R.string.read_exception)
            is ParseError -> app.getString(R.string.parse_error)
            is ParseJsonException -> app.getString(R.string.parse_json_error)
            is RequestParamsException -> app.getString(R.string.request_error)
            is ServerResponseException -> app.getString(R.string.server_error)
            is ResponseException -> msg
            else -> {
                printStackTrace()
                app.getString(R.string.other_error)
            }
        }

        Toast.makeText(app, message, Toast.LENGTH_SHORT).show()
    }

}


fun Application.initNet(host: String, block: KalleConfig.Builder.() -> Unit = {}) {
    NetConfig.host = host
    NetConfig.app = this
    val builder = KalleConfig.newBuilder()
    builder.block()
    Kalle.setConfig(builder.build())
}


/**
 * 处理错误信息
 * @receiver KalleConfig.Builder
 * @param block [@kotlin.ExtensionFunctionType] Function1<Throwable, Unit>
 */
fun KalleConfig.Builder.onError(block: Throwable.() -> Unit) {
    NetConfig.onError = block
}

/**
 * 处理PageObserver的错误信息
 * @receiver KalleConfig.Builder
 * @param block [@kotlin.ExtensionFunctionType] Function2<Throwable, [@kotlin.ParameterName] PageRefreshLayout, Unit>
 */
fun KalleConfig.Builder.onPageError(block: Throwable.(pageRefreshLayout: PageRefreshLayout) -> Unit) {
    PageObserver.onPageError = block
}


/**
 * 设置DialogObserver默认弹出的加载对话框
 * @receiver KalleConfig.Builder
 * @param block [@kotlin.ExtensionFunctionType] Function2<DialogObserver<*>, [@kotlin.ParameterName] FragmentActivity, Dialog>
 */
fun KalleConfig.Builder.onDialog(block: (DialogObserver<*>.(context: FragmentActivity) -> Dialog)) {
    DialogObserver.defaultDialog = block
}

