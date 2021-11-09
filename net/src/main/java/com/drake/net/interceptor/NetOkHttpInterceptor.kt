package com.drake.net.interceptor

import com.drake.net.body.toNetRequestBody
import com.drake.net.body.toNetResponseBody
import com.drake.net.exception.HttpFailureException
import com.drake.net.exception.NetConnectException
import com.drake.net.exception.NetSocketTimeoutException
import com.drake.net.exception.NetUnknownHostException
import com.drake.net.okhttp.attachToNet
import com.drake.net.okhttp.detachFromNet
import com.drake.net.request.downloadListeners
import com.drake.net.request.uploadListeners
import okhttp3.Interceptor
import okhttp3.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Net代理OkHttp的拦截器
 */
object NetOkHttpInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val netRequestBody = request.body?.toNetRequestBody(request.uploadListeners())
        request = request.newBuilder().method(request.method, netRequestBody).build()
        val response = try {
            chain.call().attachToNet()
            chain.proceed(request)
        } catch (e: SocketTimeoutException) {
            throw NetSocketTimeoutException(request, e.message, e)
        } catch (e: ConnectException) {
            throw NetConnectException(request, cause = e)
        } catch (e: UnknownHostException) {
            throw NetUnknownHostException(request, message = e.message)
        } catch (e: Throwable) {
            throw HttpFailureException(request, cause = e)
        }
        val netResponseBody = response.body?.toNetResponseBody(request.downloadListeners()) {
            chain.call().detachFromNet()
        }
        return response.newBuilder().body(netResponseBody).build()
    }
}