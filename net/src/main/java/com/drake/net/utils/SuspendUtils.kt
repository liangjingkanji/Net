/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：12/19/19 6:20 PM
 */

package com.drake.net.utils

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext


// <editor-fold desc="切换调度器">

suspend fun <T> withMain(block: suspend CoroutineScope.() -> T) =
    withContext(Dispatchers.Main, block)

suspend fun <T> withIO(block: suspend CoroutineScope.() -> T) =
    withContext(Dispatchers.IO, block)

suspend fun <T> withDefault(block: suspend CoroutineScope.() -> T) =
    withContext(Dispatchers.Default, block)

suspend fun <T> withUnconfined(block: suspend CoroutineScope.() -> T) =
    withContext(Dispatchers.Unconfined, block)

// </editor-fold>

/**
 * 允许抛出任何异常的作用域, 不会导致父协程取消和崩溃
 */
suspend fun tryScope(
    error: suspend CoroutineScope.(Throwable) -> Unit = { it.printStackTrace() },
    block: suspend CoroutineScope.() -> Unit
) {
    return supervisorScope {
        try {
            block()
        } catch (e: Exception) {
            error(e)
        }
    }
}

/**
 * 在主线程运行
 */
fun runMain(block: () -> Unit) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        block()
    } else {
        Handler(Looper.getMainLooper()).post { block() }
    }
}