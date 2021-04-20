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

import com.drake.net.Net
import com.drake.net.NetConfig
import com.drake.net.transform.DeferredTransform
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.CancellationException


/**
 * 该函数将选择[listDeferred]中的Deferred执行[Deferred.await], 然后将返回最快的结果
 * 执行过程中的异常将被忽略, 如果全部抛出异常则将抛出最后一个Deferred的异常
 *
 * @param group 指定该值将在成功返回结果后取消掉对应uid的网络请求
 * @param listDeferred 一系列并发任务
 */
@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("SuspendFunctionOnCoroutineScope")
suspend fun <T> CoroutineScope.fastest(
    listDeferred: List<Deferred<T>>,
    group: Any? = null
): T {
    val deferred = CompletableDeferred<T>()
    if (listDeferred.isNullOrEmpty()) {
        deferred.completeExceptionally(IllegalArgumentException("Fastest is null or empty"))
    }
    val mutex = Mutex()
    listDeferred.forEach {
        launch(Dispatchers.IO) {
            try {
                val result = it.await()
                mutex.withLock {
                    Net.cancelGroup(group)
                    deferred.complete(result)
                }
            } catch (e: Exception) {
                it.cancel()
                val allFail = listDeferred.all { it.isCancelled }
                if (allFail) deferred.completeExceptionally(e) else {
                    if (e !is CancellationException) {
                        if (NetConfig.logEnabled) e.printStackTrace()
                    }
                }
            }
        }
    }
    return deferred.await()
}


/**
 * 该函数将选择[listDeferred]中的Deferred执行[Deferred.await], 然后将返回最快的结果
 * 执行过程中的异常将被忽略, 如果全部抛出异常则将抛出最后一个Deferred的异常
 *
 * @see DeferredTransform 允许监听[Deferred]返回数据回调
 *
 * @param group 指定该值将在成功返回结果后取消掉对应uid的网络请求
 * @param listDeferred 一系列并发任务
 */
@JvmName("fastestTransform")
@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("SuspendFunctionOnCoroutineScope")
suspend fun <T, R> CoroutineScope.fastest(
    listDeferred: List<DeferredTransform<T, R>>?,
    group: Any? = null
): R {
    val deferred = CompletableDeferred<R>()
    if (listDeferred.isNullOrEmpty()) {
        deferred.completeExceptionally(IllegalArgumentException("Fastest is null or empty"))
    }
    val mutex = Mutex()
    listDeferred?.forEach {
        launch(Dispatchers.IO) {
            try {
                val result = it.deferred.await()
                mutex.withLock {
                    Net.cancelGroup(group)
                    if (!deferred.isCompleted) {
                        val transformResult = it.block(result)
                        deferred.complete(transformResult)
                    }
                }
            } catch (e: Exception) {
                it.deferred.cancel()
                val allFail = listDeferred.all { it.deferred.isCancelled }
                if (allFail) deferred.completeExceptionally(e) else {
                    if (e !is CancellationException) {
                        if (NetConfig.logEnabled) e.printStackTrace()
                    }
                }
            }
        }
    }
    return deferred.await()
}