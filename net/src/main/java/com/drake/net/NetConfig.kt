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
import com.drake.net.error.RequestParamsException
import com.drake.net.error.ResponseException
import com.drake.net.error.ServerResponseException
import com.drake.net.scope.DialogCoroutineScope
import com.drake.tooltip.toast
import com.yanzhenjie.kalle.Kalle
import com.yanzhenjie.kalle.KalleConfig
import com.yanzhenjie.kalle.connect.BroadcastNetwork
import com.yanzhenjie.kalle.exception.*
import com.yanzhenjie.kalle.simple.cache.DiskCacheStore
import java.util.concurrent.ExecutionException


/**
 * Net的全局配置
 *
 * @property host 全局的域名或者ip(baseUrl)
 * @property app 全局上下文, 一般执行[initNet]即可, 无需手动赋值
 * @property onDialog 全局加载框
 * @property onError 全局错误处理
 * @property onStateError 全局缺省页错误处理
 */
object NetConfig {

    lateinit var host: String
    lateinit var app: Application

    var onDialog: DialogCoroutineScope.(FragmentActivity) -> Dialog = {
        val progress = ProgressDialog(activity)
        progress.setMessage(activity.getString(R.string.net_dialog_msg))
        progress
    }

    var onError: Throwable.() -> Unit = {
        val message = when (this) {
            is NetworkError -> app.getString(R.string.net_network_error)
            is URLError -> app.getString(R.string.net_url_error)
            is HostError -> app.getString(R.string.net_host_error)
            is ConnectTimeoutError -> app.getString(R.string.net_connect_timeout_error)
            is ReadTimeoutError -> app.getString(R.string.net_read_timeout_error)
            is DownloadError -> app.getString(R.string.net_download_error)
            is NoCacheError -> app.getString(R.string.net_no_cache_error)
            is ParseError -> app.getString(R.string.net_parse_error)
            is RequestParamsException -> app.getString(R.string.net_request_error)
            is ServerResponseException -> app.getString(R.string.net_server_error)
            is ExecutionException -> app.getString(R.string.net_image_error)
            is NullPointerException -> app.getString(R.string.net_null_error)
            is ConnectException -> app.getString(R.string.net_connect_exception)
            is WriteException -> app.getString(R.string.net_write_exception)
            is ReadException -> app.getString(R.string.net_read_exception)
            is ResponseException -> msg
            else -> app.getString(R.string.net_other_error)
        }

        printStackTrace()
        app.toast(message)
    }

    var onStateError: Throwable.(view: View) -> Unit = {
        when (this) {
            is ParseError,
            is RequestParamsException,
            is ResponseException,
            is NullPointerException -> onError(this)
            else -> printStackTrace()
        }
    }
}


/**
 * 初始化框架
 * @param host 请求url的主机名
 * @param config 进行配置网络请求
 *
 * 如果想要自动解析数据模型请配置转换器, 可以继承或者参考默认转换器
 *
 * @see com.drake.net.convert.DefaultConvert
 */
fun Application.initNet(host: String, config: KalleConfig.Builder.() -> Unit = {}) {
    NetConfig.host = host
    NetConfig.app = this
    val builder = KalleConfig.newBuilder()
    builder.apply {
        network(BroadcastNetwork(this@initNet))
        config()
    }
    Kalle.setConfig(builder.build())
}


/**
 * 该函数指定某些Observer的onError中的默认错误信息处理
 *
 * @see NetConfig.onError
 */
fun KalleConfig.Builder.onError(block: Throwable.() -> Unit) {
    NetConfig.onError = block
}

/**
 * 该函数指定某些Observer的onError中的默认错误信息处理
 *
 * 如果不设置默认只有 解析数据错误 | 后台自定义错误 会显示吐司
 * @see NetConfig.onStateError
 */
fun KalleConfig.Builder.onStateError(block: Throwable.(view: View) -> Unit) {
    NetConfig.onStateError = block
}


/**
 * 设置使用DialogObserver默认弹出的加载对话框
 * 默认使用系统自带的ProgressDialog
 */
fun KalleConfig.Builder.onDialog(block: (DialogCoroutineScope.(context: FragmentActivity) -> Dialog)) {
    NetConfig.onDialog = block
}

/**
 * 开启缓存
 * @param path 缓存数据库路径
 * @param password 缓存数据库密码
 */
fun KalleConfig.Builder.cacheEnabled(
    path: String = NetConfig.app.cacheDir.absolutePath,
    password: String = "cache"
) {
    val cacheStore = DiskCacheStore.newBuilder(path)
        .password(password)
        .build()
    cacheStore(cacheStore)
}

