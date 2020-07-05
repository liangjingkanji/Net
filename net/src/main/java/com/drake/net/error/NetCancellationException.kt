/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：6/24/20 1:35 AM
 */

package com.drake.net.error

import com.yanzhenjie.kalle.NetCancel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.CancellationException

class NetCancellationException(coroutineScope: CoroutineScope,
                               message: String? = null) : CancellationException(message) {
    init {
        NetCancel.cancel(coroutineScope.coroutineContext[CoroutineExceptionHandler])
    }
}


/**
 * 抛出该异常将取消作用域内所有的网络请求
 */
@Suppress("FunctionName")
fun CoroutineScope.NetCancellationException(message: String? = null): NetCancellationException {
    return NetCancellationException(this, message)
}