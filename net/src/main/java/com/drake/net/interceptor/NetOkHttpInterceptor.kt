package com.drake.net.interceptor

import com.drake.net.body.toNetRequestBody
import com.drake.net.body.toNetResponseBody
import com.drake.net.exception.NetConnectException
import com.drake.net.exception.NetException
import com.drake.net.exception.NetSocketTimeoutException
import com.drake.net.exception.NetUnknownHostException
import com.drake.net.okhttp.attachToNet
import com.drake.net.okhttp.detachFromNet
import com.drake.net.request.downloadListeners
import com.drake.net.request.setLabel
import com.drake.net.request.uploadListeners
import com.drake.net.tag.NetLabel
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
        val netRequestBody = request.body?.toNetRequestBody(request)
        request = request.newBuilder().method(request.method, netRequestBody).apply {
            if (request.uploadListeners() == null) setLabel(NetLabel.UploadListeners())
            if (request.downloadListeners() == null) setLabel(NetLabel.DownloadListeners())
        }.build()
        val response = try {
            chain.call().attachToNet()
            chain.proceed(request)
        } catch (e: SocketTimeoutException) {
            throw NetSocketTimeoutException(request, e.message, e)
        } catch (e: ConnectException) {
            throw NetConnectException(request, cause = e)
        } catch (e: UnknownHostException) {
            throw NetUnknownHostException(request, cause = e)
        } catch (e: Throwable) {
            throw NetException(request, cause = e)
        }
        val netResponseBody = response.body?.toNetResponseBody(request) {
            chain.call().detachFromNet()
        }
        return response.newBuilder().body(netResponseBody).build()
    }
}