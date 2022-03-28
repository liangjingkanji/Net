package com.drake.net.okhttp

import com.drake.net.NetConfig
import com.drake.net.convert.NetConverter
import com.drake.net.interceptor.NetOkHttpInterceptor
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.interfaces.NetDialogFactory
import com.drake.net.interfaces.NetErrorHandler
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
 * Net要求经过该函数处理创建特殊的OkHttpClient
 */
fun OkHttpClient.Builder.toNetOkhttp() = apply {
    val interceptors = interceptors()
    if (!interceptors.contains(NetOkHttpInterceptor)) {
        addInterceptor(NetOkHttpInterceptor)
    }
}


/**
 * 配置信任所有证书
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
 * 配置信任所有证书
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
 * 全局错误处理器
 */
fun OkHttpClient.Builder.setErrorHandler(handler: NetErrorHandler) = apply {
    NetConfig.errorHandler = handler
}

/**
 * 请求对话框构建工厂
 */
fun OkHttpClient.Builder.setDialogFactory(dialogFactory: NetDialogFactory) = apply {
    NetConfig.dialogFactory = dialogFactory
}