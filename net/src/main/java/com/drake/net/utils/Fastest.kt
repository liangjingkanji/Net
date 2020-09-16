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

import com.drake.net.transform.DeferredTransform
import com.yanzhenjie.kalle.NetCancel
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


/**
 * 该函数将选择[deferredArray]中的Deferred执行[Deferred.await], 然后将返回最快的结果
 * 执行过程中的异常将被忽略, 如果全部抛出异常则将抛出最后一个Deferred的异常
 *
 * @param deferredArray 一系列并发任务
 */
@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("SuspendFunctionOnCoroutineScope")
suspend fun <T> CoroutineScope.fastest(vararg deferredArray: Deferred<T>): T {
    val deferred = CompletableDeferred<T>()
    val mutex = Mutex()
    deferredArray.forEach {
        launch(Dispatchers.IO) {
            try {
                val result = it.await()
                mutex.withLock {
                    NetCancel.cancel(coroutineContext[CoroutineExceptionHandler])
                    deferred.complete(result)
                }
            } catch (e: Exception) {
                it.cancel()
                val allFail = deferredArray.all { it.isCancelled }
                if (allFail) deferred.completeExceptionally(e) else {
                    if (e !is CancellationException) e.printStackTrace()
                }
            }
        }
    }
    return deferred.await()
}

/**
 * 该函数将选择[deferredArray]中的Deferred执行[Deferred.await], 然后将返回最快的结果
 * 执行过程中的异常将被忽略, 如果全部抛出异常则将抛出最后一个Deferred的异常
 *
 * @param deferredArray 一系列并发任务
 */
@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("SuspendFunctionOnCoroutineScope")
suspend fun <T> CoroutineScope.fastest(deferredArray: List<Deferred<T>>): T {
    val deferred = CompletableDeferred<T>()
    val mutex = Mutex()
    deferredArray.forEach {
        launch(Dispatchers.IO) {
            try {
                val result = it.await()
                mutex.withLock {
                    NetCancel.cancel(coroutineContext[CoroutineExceptionHandler])
                    deferred.complete(result)
                }
            } catch (e: Exception) {
                it.cancel()
                val allFail = deferredArray.all { it.isCancelled }
                if (allFail) deferred.completeExceptionally(e) else {
                    if (e !is CancellationException) e.printStackTrace()
                }
            }
        }
    }
    return deferred.await()
}

/**
 * 该函数将选择[deferredArray]中的Deferred执行[Deferred.await], 然后将返回最快的结果
 * 执行过程中的异常将被忽略, 如果全部抛出异常则将抛出最后一个Deferred的异常
 *
 * @see DeferredTransform 允许监听[Deferred]返回数据回调
 * @param deferredArray 一系列并发任务
 */
@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("SuspendFunctionOnCoroutineScope")
suspend fun <T, R> CoroutineScope.fastest(vararg deferredArray: DeferredTransform<T, R>): R {
    val deferred = CompletableDeferred<R>()
    val mutex = Mutex()
    deferredArray.forEach {
        launch(Dispatchers.IO) {
            try {
                val result = it.deferred.await()
                mutex.withLock {
                    NetCancel.cancel(coroutineContext[CoroutineExceptionHandler])
                    if (!deferred.isCompleted) {
                        val transformResult = it.block(result)
                        deferred.complete(transformResult)
                    }
                }
            } catch (e: Exception) {
                it.deferred.cancel()
                val allFail = deferredArray.all { it.deferred.isCancelled }
                if (allFail) deferred.completeExceptionally(e) else {
                    if (e !is CancellationException) e.printStackTrace()
                }
            }
        }
    }
    return deferred.await()
}

/**
 * 该函数将选择[deferredList]中的Deferred执行[Deferred.await], 然后将返回最快的结果
 * 执行过程中的异常将被忽略, 如果全部抛出异常则将抛出最后一个Deferred的异常
 *
 * @see DeferredTransform 允许监听[Deferred]返回数据回调
 * @param deferredList 一系列并发任务
 */
@JvmName("fastestTransform")
@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("SuspendFunctionOnCoroutineScope")
suspend fun <T, R> CoroutineScope.fastest(deferredList: List<DeferredTransform<T, R>>): R {
    val deferred = CompletableDeferred<R>()
    val mutex = Mutex()
    deferredList.forEach {
        launch(Dispatchers.IO) {
            try {
                val result = it.deferred.await()
                mutex.withLock {
                    NetCancel.cancel(coroutineContext[CoroutineExceptionHandler])
                    if (!deferred.isCompleted) {
                        val transformResult = it.block(result)
                        deferred.complete(transformResult)
                    }
                }
            } catch (e: Exception) {
                it.deferred.cancel()
                val allFail = deferredList.all { it.deferred.isCancelled }
                if (allFail) deferred.completeExceptionally(e) else {
                    if (e !is CancellationException) e.printStackTrace()
                }
            }
        }
    }
    return deferred.await()
}