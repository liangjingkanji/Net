package com.drake.net.interfaces

import android.view.View
import com.drake.net.NetConfig

interface NetErrorHandler {

    companion object DEFAULT : NetErrorHandler

    /**
     * 全局错误
     * @param e 发生的错误
     */
    fun onError(e: Throwable) = NetConfig.onError(e)

    /**
     * 自动缺省页错误
     * @param e 发生的错误
     * @param view 缺省页, StateLayout或者PageRefreshLayout
     */
    fun onStateError(e: Throwable, view: View) = NetConfig.onStateError(e, view)
}