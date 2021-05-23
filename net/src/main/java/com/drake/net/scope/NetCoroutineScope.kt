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

package com.drake.net.scope

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.drake.net.Net
import com.drake.net.NetConfig
import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext


/**
 * 自动显示网络错误信息协程作用域
 */
@Suppress("unused", "MemberVisibilityCanBePrivate", "NAME_SHADOWING")
open class NetCoroutineScope(
    lifecycleOwner: LifecycleOwner? = null,
    lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    dispatcher: CoroutineDispatcher = Dispatchers.Main
) : AndroidScope(lifecycleOwner, lifeEvent, dispatcher) {

    protected var isReadCache = true
    protected var preview: (suspend CoroutineScope.() -> Unit)? = null

    protected var isCacheSucceed = false
        get() = if (preview != null) field else false

    protected var error = true
        get() = if (isCacheSucceed) field else true

    var animate: Boolean = false


    override fun launch(block: suspend CoroutineScope.() -> Unit): NetCoroutineScope {
        launch(EmptyCoroutineContext) {
            start()
            if (preview != null && isReadCache) {
                supervisorScope {
                    isCacheSucceed = try {
                        preview?.invoke(this)
                        true
                    } catch (e: Exception) {
                        false
                    }
                    readCache(isCacheSucceed)
                }
            }
            block()
        }.invokeOnCompletion {
            finally(it)
        }
        return this
    }

    protected open fun start() {

    }

    /**
     * 读取缓存回调
     * @param succeed 缓存是否成功
     */
    protected open fun readCache(succeed: Boolean) {}


    override fun handleError(e: Throwable) {
        NetConfig.errorHandler.onError(e)
    }

    override fun catch(e: Throwable) {
        catch?.invoke(this@NetCoroutineScope, e) ?: if (error) handleError(e)
    }

    /**
     * "预览"作用域
     * 该函数一般用于缓存读取, 只在第一次启动作用域时回调
     * 该函数在作用域[NetCoroutineScope.launch]之前执行
     * 函数内部所有的异常都不会被抛出, 也不会终止作用域执行
     *
     * @param ignore 是否在缓存读取成功但网络请求错误时吐司错误信息
     * @param animate 是否在缓存成功后依然显示加载动画
     * @param block 该作用域内的所有异常都算缓存读取失败, 不会吐司和打印任何错误
     */
    fun preview(
        ignore: Boolean = false,
        animate: Boolean = false,
        block: suspend CoroutineScope.() -> Unit
    ): AndroidScope {
        this.animate = animate
        this.error = ignore
        this.preview = block
        return this
    }

    override fun cancel(cause: CancellationException?) {
        Net.cancelGroup(scopeGroup)
        super.cancel(cause)
    }
}