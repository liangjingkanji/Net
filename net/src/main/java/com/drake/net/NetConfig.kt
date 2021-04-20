/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drake.net

import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog
import android.app.ProgressDialog
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.drake.net.NetConfig.app
import com.drake.net.NetConfig.host
import com.drake.net.NetConfig.onDialog
import com.drake.net.NetConfig.onError
import com.drake.net.NetConfig.onStateError
import com.drake.net.convert.NetConverter
import com.drake.net.exception.*
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.scope.DialogCoroutineScope
import com.drake.tooltip.toast
import okhttp3.Call
import okhttp3.OkHttpClient
import java.lang.ref.WeakReference
import java.net.UnknownHostException
import java.util.concurrent.*


/**
 * Net的全局配置
 *
 * @property host 全局的域名或者ip(baseUrl)
 * @property app 全局上下文, 一般执行[initNet]即可, 无需手动赋值
 * @property onDialog 全局加载框
 * @property onError 全局错误处理
 * @property onStateError 全局缺省页错误处理
 */
@SuppressLint("StaticFieldLeak")
object NetConfig {

    lateinit var app: Application
    lateinit var okHttpClient: OkHttpClient

    var host: String = ""
    var logEnabled = true
    var runningCalls: ConcurrentLinkedQueue<WeakReference<Call>> = ConcurrentLinkedQueue()
    var converter: NetConverter = NetConverter.DEFAULT
    var requestInterceptor: RequestInterceptor? = null

    var onDialog: DialogCoroutineScope.(FragmentActivity) -> Dialog = {
        val progress = ProgressDialog(activity)
        progress.setMessage(activity.getString(R.string.net_dialog_msg))
        progress
    }

    var onError: Throwable.() -> Unit = onError@{

        val message = when (this) {
            is UnknownHostException -> app.getString(R.string.net_host_error)
            is URLParseException -> app.getString(R.string.net_url_error)
            is NetConnectException -> app.getString(R.string.net_network_error)
            is NetSocketTimeoutException -> app.getString(R.string.net_connect_timeout_error, message)
            is DownloadFileException -> app.getString(R.string.net_download_error)
            is ConvertException -> app.getString(R.string.net_parse_error)
            is RequestParamsException -> app.getString(R.string.net_request_error)
            is ServerResponseException -> app.getString(R.string.net_server_error)
            is NullPointerException -> app.getString(R.string.net_null_error)
            is NoCacheException -> app.getString(R.string.net_no_cache_error)
            is ResponseException -> message
            is NetException -> app.getString(R.string.net_error)
            else -> app.getString(R.string.net_other_error)
        }

        if (logEnabled) printStackTrace()
        app.toast(message)
    }

    var onStateError: Throwable.(view: View) -> Unit = {
        when (this) {
            is ConvertException,
            is RequestParamsException,
            is ResponseException,
            is NullPointerException -> onError(this)
            else -> if (logEnabled) printStackTrace()
        }
    }
}

