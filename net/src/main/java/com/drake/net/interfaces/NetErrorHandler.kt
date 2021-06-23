package com.drake.net.interfaces

import android.view.View
import com.drake.net.NetConfig

interface NetErrorHandler {

    /**
     * 全局错误
     */
    fun onError(e: Throwable) = NetConfig.onError(e)

    /**
     * 自动缺省页错误
     */
    fun onStateError(e: Throwable, view: View) = NetConfig.onStateError(e, view)

    companion object DEFAULT : NetErrorHandler
}