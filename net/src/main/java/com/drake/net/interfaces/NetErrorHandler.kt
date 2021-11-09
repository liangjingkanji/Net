package com.drake.net.interfaces

import android.view.View
import com.drake.net.NetConfig
import com.drake.net.R
import com.drake.net.exception.*
import com.drake.net.utils.TipUtils
import java.net.UnknownHostException

interface NetErrorHandler {

    companion object DEFAULT : NetErrorHandler

    /**
     * 全局错误
     * @param e 发生的错误
     */
    fun onError(e: Throwable) {
        val message = when (e) {
            is UnknownHostException -> NetConfig.app.getString(R.string.net_host_error)
            is URLParseException -> NetConfig.app.getString(R.string.net_url_error)
            is NetConnectException -> NetConfig.app.getString(R.string.net_network_error)
            is NetSocketTimeoutException -> NetConfig.app.getString(
                R.string.net_connect_timeout_error,
                e.message
            )
            is DownloadFileException -> NetConfig.app.getString(R.string.net_download_error)
            is ConvertException -> NetConfig.app.getString(R.string.net_parse_error)
            is RequestParamsException -> NetConfig.app.getString(R.string.net_request_error)
            is ServerResponseException -> NetConfig.app.getString(R.string.net_server_error)
            is NullPointerException -> NetConfig.app.getString(R.string.net_null_error)
            is NoCacheException -> NetConfig.app.getString(R.string.net_no_cache_error)
            is ResponseException -> e.message
            is HttpFailureException -> NetConfig.app.getString(R.string.request_failure)
            is NetException -> NetConfig.app.getString(R.string.net_error)
            else -> NetConfig.app.getString(R.string.net_other_error)
        }

        if (NetConfig.logEnabled) e.printStackTrace()
        TipUtils.toast(message)
    }

    /**
     * 自动缺省页错误
     * @param e 发生的错误
     * @param view 缺省页, StateLayout或者PageRefreshLayout
     */
    fun onStateError(e: Throwable, view: View) {
        when (e) {
            is ConvertException,
            is RequestParamsException,
            is ResponseException,
            is NullPointerException -> onError(e)
            else -> if (NetConfig.logEnabled) e.printStackTrace()
        }
    }
}