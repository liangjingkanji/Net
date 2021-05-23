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

package com.drake.net.okhttp

import android.app.Dialog
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.drake.net.NetConfig
import com.drake.net.convert.NetConverter
import com.drake.net.interceptor.NetOkHttpInterceptor
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.interfaces.NetErrorHandler
import com.drake.net.request.label
import com.drake.net.scope.DialogCoroutineScope
import com.drake.net.tag.NetLabel
import com.drake.net.utils.Https
import com.drake.net.utils.chooseTrustManager
import com.drake.net.utils.prepareKeyManager
import com.drake.net.utils.prepareTrustManager
import okhttp3.OkHttpClient
import java.io.InputStream
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * 开启日志
 */
fun OkHttpClient.Builder.setLog(enabled: Boolean) = apply {
    NetConfig.logEnabled = enabled
}

/**
 * 设置全局默认的Host, 在使用[com.drake.net.request.BaseRequest.setPath]的时候会成为默认的Host
 */
fun OkHttpClient.Builder.setHost(host: String) = apply {
    NetConfig.host = host
}

/**
 * Net拦截器代理OkHttp
 */
fun OkHttpClient.Builder.toNetOkhttp() = apply {
    val interceptors = interceptors()
    if (!interceptors.contains(NetOkHttpInterceptor)) {
        addInterceptor(NetOkHttpInterceptor)
    }
}

fun OkHttpClient.toNetOkhttp() = run {
    if (!interceptors.contains(NetOkHttpInterceptor)) {
        newBuilder().addInterceptor(NetOkHttpInterceptor).build()
    } else this
}

/**
 * @param trustManager 如果需要自己校验，那么可以自己实现相关校验，如果不需要自己校验，那么传null即可
 * @param bksFile  客户端使用bks证书校验服务端证书
 * @param password bks证书的密码
 */
fun OkHttpClient.Builder.setSSLCertificate(
    trustManager: X509TrustManager?,
    bksFile: InputStream? = null,
    password: String? = null,
) = apply {
    try {
        val trustManagerFinal: X509TrustManager = trustManager ?: Https.UnSafeTrustManager

        val keyManagers = prepareKeyManager(bksFile, password)
        val sslContext = SSLContext.getInstance("TLS")
        // 用上面得到的trustManagers初始化SSLContext，这样sslContext就会信任keyStore中的证书
        // 第一个参数是授权的密钥管理器，用来授权验证，比如授权自签名的证书验证。第二个是被授权的证书管理器，用来验证服务器端的证书
        sslContext.init(keyManagers, arrayOf<TrustManager?>(trustManagerFinal), null)
        // 通过sslContext获取SSLSocketFactory对象

        sslSocketFactory(sslContext.socketFactory, trustManagerFinal)
    } catch (e: NoSuchAlgorithmException) {
        throw AssertionError(e)
    } catch (e: KeyManagementException) {
        throw AssertionError(e)
    }
}

/**
 * @param certificates 含有服务端公钥的证书校验服务端证书
 * @param bksFile  客户端使用bks证书校验服务端证书
 * @param password bks证书的密码
 */
fun OkHttpClient.Builder.setSSLCertificate(
    vararg certificates: InputStream,
    bksFile: InputStream? = null,
    password: String? = null
) = apply {
    val trustManager = prepareTrustManager(*certificates)?.let { chooseTrustManager(it) }
    setSSLCertificate(trustManager, bksFile, password)
}


/**
 * 信任所有证书
 */
fun OkHttpClient.Builder.trustSSLCertificate() = apply {
    hostnameVerifier(Https.UnSafeHostnameVerifier)
    setSSLCertificate(null)
}

/**
 * 转换器
 */
fun OkHttpClient.Builder.setConverter(converter: NetConverter) = apply {
    NetConfig.converter = converter
}

/**
 * 添加轻量级的请求拦截器, 可以在每次请求之前修改参数或者客户端配置
 * 该拦截器不同于OkHttp的Interceptor无需处理请求动作
 */
fun OkHttpClient.Builder.setRequestInterceptor(interceptor: RequestInterceptor) = apply {
    NetConfig.requestInterceptor = interceptor
}

/**
 * 全局网络请求错误捕获
 */
@Deprecated(message = "使用NetErrorHandler统一处理错误", replaceWith = ReplaceWith("setErrorHandler(NetErrorHandler())"), DeprecationLevel.WARNING)
fun OkHttpClient.Builder.onError(block: Throwable.() -> Unit) = apply {
    NetConfig.onError = block
}

/**
 * 全局缺省页错误捕获
 */
@Deprecated(message = "使用NetErrorHandler统一处理错误", replaceWith = ReplaceWith("setErrorHandler(NetErrorHandler())"), DeprecationLevel.WARNING)
fun OkHttpClient.Builder.onStateError(block: Throwable.(view: View) -> Unit) = apply {
    NetConfig.onStateError = block
}

/**
 * 全局错误处理器
 *
 * 会覆盖[onError]和[onStateError]
 */
fun OkHttpClient.Builder.setErrorHandler(handler: NetErrorHandler) = apply {
    NetConfig.errorHandler = handler
}

/**
 * 全局加载对话框设置
 * 设置在使用scopeDialog自动弹出的加载对话框
 */
fun OkHttpClient.Builder.onDialog(block: DialogCoroutineScope.(FragmentActivity) -> Dialog) =
    apply {
        NetConfig.onDialog = block
    }

/**
 * 取消OkHttp客户端中指定Id的请求
 * 如果使用的是Net创建的网络请求请使用[com.drake.net.Net.cancelId]
 */
fun OkHttpClient.cancelId(id: Any?) {
    id ?: return
    dispatcher.runningCalls().forEach {
        if (id === it.request().label<NetLabel.RequestId>()?.value) {
            it.cancel()
        }
    }
    dispatcher.queuedCalls().forEach {
        if (id === it.request().label<NetLabel.RequestId>()?.value) {
            it.cancel()
        }
    }
}

/**
 * 取消OkHttp客户端中指定Group的请求
 * 如果使用的是Net创建的网络请求请使用[com.drake.net.Net.cancelGroup]
 */
fun OkHttpClient.cancelGroup(group: Any?) {
    group ?: return
    dispatcher.runningCalls().forEach {
        if (group === it.request().label<NetLabel.RequestGroup>()?.value) {
            it.cancel()
        }
    }
    dispatcher.queuedCalls().forEach {
        if (group === it.request().label<NetLabel.RequestGroup>()?.value) {
            it.cancel()
        }
    }
}
