/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

package com.drake.net.observer

import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.drake.net.NetConfig
import com.drake.net.exception.ResponseException
import com.yanzhenjie.kalle.exception.*
import io.reactivex.observers.DefaultObserver


/**
 * 自动显示错误信息
 */
abstract class NetObserver<M>() : DefaultObserver<M>(), LifecycleObserver {

    constructor(lifecycleOwner: LifecycleOwner?) : this() {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    /**
     * 关闭进度对话框并提醒错误信息
     *
     * @param e 包括错误信息
     */
    override fun onError(e: Throwable) {
        showErrorMsg(e)
    }

    override fun onComplete() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cancelRequest() {
        cancel()
    }

    companion object {

        fun showErrorMsg(e: Throwable) {
            val message: String
            when (e) {
                is NetworkError -> message = "当前网络不可用"
                is URLError -> message = "请求资源地址错误"
                is HostError -> message = "无法找到指定服务器主机"
                is ConnectTimeoutError -> message = "连接服务器超时，请重试"
                is ConnectException -> message = "请检查网络连接"
                is WriteException -> message = "发送数据错误"
                is ReadTimeoutError -> message = "读取服务器数据超时，请检查网络"
                is DownloadError -> message = "下载过程发生错误"
                is NoCacheError -> message = "读取缓存错误"
                is ParseError -> message = "解析数据时发生异常"
                is ReadException -> message = "读取数据错误"
                is ResponseException -> message = e.errorMessage
                else -> {
                    e.printStackTrace()
                    message = "服务器未响应"
                }
            }
            Toast.makeText(NetConfig.App, message, Toast.LENGTH_SHORT).show()
        }
    }
}
