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
import com.drake.net.cache.ForceCache
import com.drake.net.convert.NetConverter
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.interfaces.NetDialogFactory
import com.drake.net.interfaces.NetErrorHandler
import com.drake.net.okhttp.toNetOkhttp
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.OkHttpUtils
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentLinkedQueue


/**
 * Net的全局配置
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
            forceCache = field.cache?.let { ForceCache(OkHttpUtils.diskLruCache(it)) }
        }

    /**
     * 强制缓存配置. 不允许直接设置, 因为整个框架只允许存在一个缓存配置管理, 所以请使用[OkHttpClient.Builder.cache]
     * 强制缓存会无视标准Http协议强制缓存任何数据
     * 缓存目录和缓存最大值设置见: [okhttp3.Cache]
     */
    internal var forceCache: ForceCache? = null

    /** 是否启用日志 */
    @Deprecated("命名变更", ReplaceWith("NetConfig.debug"))
    var logEnabled
        get() = debug
        set(value) {
            debug = value
        }

    /** 是否启用日志 */
    var debug = true

    /** 网络异常日志的标签 */
    @Deprecated("命名变更", ReplaceWith("NetConfig.TAG"))
    var logTag
        get() = TAG
        set(value) {
            TAG = value
        }

    /** 网络异常日志的标签 */
    var TAG = "NET_LOG"

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

