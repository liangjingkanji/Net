/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drake.net.utils

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


// <editor-fold desc="切换调度器">

/**
 * 切换到主线程调度器
 */
suspend fun <T> withMain(block: suspend CoroutineScope.() -> T) = withContext(Dispatchers.Main, block)

/**
 * 切换到IO程调度器
 */
suspend fun <T> withIO(block: suspend CoroutineScope.() -> T) = withContext(Dispatchers.IO, block)

/**
 * 切换到默认调度器
 */
suspend fun <T> withDefault(block: suspend CoroutineScope.() -> T) = withContext(Dispatchers.Default, block)

/**
 * 切换到没有限制的调度器
 */
suspend fun <T> withUnconfined(block: suspend CoroutineScope.() -> T) = withContext(Dispatchers.Unconfined, block)

// </editor-fold>

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