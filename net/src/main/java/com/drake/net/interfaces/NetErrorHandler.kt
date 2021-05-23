package com.drake.net.interfaces

import android.view.View
import com.drake.net.NetConfig

open class NetErrorHandler {

    /**
     * 全局错误
     */
    open fun onError(e: Throwable) = NetConfig.onError(e)

    /**
     * 自动缺省页错误
     */
    open fun onStateError(e: Throwable, view: View) = NetConfig.onStateError(e, view)
}