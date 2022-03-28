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
import android.content.Context
import com.drake.net.NetConfig.app
import com.drake.net.NetConfig.converter
import com.drake.net.NetConfig.dialogFactory
import com.drake.net.NetConfig.errorHandler
import com.drake.net.NetConfig.host
import com.drake.net.NetConfig.logEnabled
import com.drake.net.NetConfig.requestInterceptor
import com.drake.net.NetConfig.runningCalls
import com.drake.net.convert.NetConverter
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.interfaces.NetDialogFactory
import com.drake.net.interfaces.NetErrorHandler
import com.drake.net.okhttp.toNetOkhttp
import okhttp3.Call
import okhttp3.OkHttpClient
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentLinkedQueue


/**
 * Net的全局配置
 *
 * @property app 全局上下文, 一般执行[NetConfig.init]即可, 无需手动赋值
 * @property host 全局的域名或者ip(baseUrl)
 * @property runningCalls Net中正在运行的请求Call
 * @property requestInterceptor 请求拦截器
 * @property logEnabled 是否启用日志
 * @property dialogFactory 全局加载框
 * @property errorHandler 全局错误处理器, 会覆盖onError/onStateError
 * @property converter 全局数据转换器
 */
@SuppressLint("StaticFieldLeak")
object NetConfig {

    lateinit var app: Context

    /** 全局域名 */
    var host: String = ""

    /** 全局单例请求客户端 */
    var okHttpClient: OkHttpClient = OkHttpClient.Builder().toNetOkhttp().build()
        set(value) {
            field = value.toNetOkhttp()
        }

    /** 是否启用日志 */
    var logEnabled = true

    /** 网络异常日志的标签 */
    var logTag = "NET-LOG"

    /** 运行中的请求 */
    var runningCalls: ConcurrentLinkedQueue<WeakReference<Call>> = ConcurrentLinkedQueue()
        private set

    /** 请求拦截器 */
    var requestInterceptor: RequestInterceptor? = null

    /** 响应数据转换器 */
    var converter: NetConverter = NetConverter

    /** 错误处理器 */
    var errorHandler: NetErrorHandler = NetErrorHandler

    /** 请求对话框构建工厂 */
    var dialogFactory: NetDialogFactory = NetDialogFactory

    //<editor-fold desc="初始化">
    /**
     * 初始化框架
     * 不初始化也可以使用, 但是App使用多进程情况下要求为[NetConfig.host]或者[context]赋值, 否则会导致无法正常吐司或其他意外问题
     * @param host 请求url的主机名, 该参数会在每次请求时自动和请求路径进行拼接(如果路径包含https/http则不会拼接)
     * @param context 如果应用存在多进程请指定此参数初始化[NetConfig.app]
     * @param config 进行配置网络请求
     */
    @Deprecated("命名规范", ReplaceWith("initialize(host, context, config)"))
    fun init(
        host: String = "",
        context: Context? = null,
        config: OkHttpClient.Builder.() -> Unit = {}
    ) = initialize(host, context, config)

    /**
     * 初始化框架
     * 不初始化也可以使用, 但是App使用多进程情况下要求为[NetConfig.host]或者[context]赋值, 否则会导致无法正常吐司或其他意外问题
     * @param host 请求url的主机名
     * @param context 如果应用存在多进程请指定此参数初始化[NetConfig.app]
     * @param config 进行配置网络请求
     */
    @Deprecated("命名规范", ReplaceWith("initialize(host, context, config)"))
    fun init(
        host: String = "",
        context: Context? = null,
        config: OkHttpClient.Builder
    ) = initialize(host, context, config)

    /**
     * 初始化框架
     * 不初始化也可以使用, 但是App使用多进程情况下要求为[NetConfig.host]或者[context]赋值, 否则会导致无法正常吐司或其他意外问题
     * @param host 请求url的主机名
     * @param context 如果应用存在多进程请指定此参数初始化[NetConfig.app]
     * @param config 进行配置网络请求
     */
    fun initialize(
        host: String = "",
        context: Context? = null,
        config: OkHttpClient.Builder.() -> Unit = {}
    ) {
        NetConfig.host = host
        context?.let { app = it }
        val builder = OkHttpClient.Builder()
        builder.config()
        okHttpClient = builder.toNetOkhttp().build()
    }

    /**
     * 初始化框架
     * 不初始化也可以使用, 但是App使用多进程情况下要求为[NetConfig.host]或者[context]赋值, 否则会导致无法正常吐司或其他意外问题
     * @param host 请求url的主机名, 该参数会在每次请求时自动和请求路径进行拼接(如果路径包含https/http则不会拼接)
     * @param context 如果应用存在多进程请指定此参数初始化[NetConfig.app]
     * @param config 进行配置网络请求
     */
    fun initialize(host: String = "", context: Context? = null, config: OkHttpClient.Builder) {
        NetConfig.host = host
        context?.let { app = it }
        okHttpClient = config.toNetOkhttp().build()
    }
    //</editor-fold>
}

