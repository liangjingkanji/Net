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

    /** 预览模式 */
    protected var preview: (suspend CoroutineScope.() -> Unit)? = null

    /** 是否启用预览 */
    protected var previewEnabled = true

    /** 是否读取缓存成功 */
    protected var previewSucceed = false
        get() = if (preview != null) field else false

    /** 使用[preview]预览模式情况下读取缓存成功后, 网络请求失败是否处理错误信息 */
    protected var previewBreakError = false
        get() = if (previewSucceed) field else false

    /** 使用[preview]预览模式情况下读取缓存成功后是否关闭加载动画 */
    protected var previewBreakLoading: Boolean = true

    override fun launch(block: suspend CoroutineScope.() -> Unit): NetCoroutineScope {
        launch(EmptyCoroutineContext) {
            start()
            if (preview != null && previewEnabled) {
                supervisorScope {
                    previewSucceed = try {
                        preview?.invoke(this)
                        true
                    } catch (e: Exception) {
                        false
                    }
                    previewFinish(previewSucceed)
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
    protected open fun previewFinish(succeed: Boolean) {
        previewEnabled = false
    }

    override fun handleError(e: Throwable) {
        NetConfig.errorHandler.onError(e)
    }

    override fun catch(e: Throwable) {
        catch?.invoke(this@NetCoroutineScope, e) ?: if (!previewBreakError) handleError(e)
    }

    /**
     * "预览"作用域
     * 该函数一般用于缓存读取, 只在第一次启动作用域时回调
     * 该函数在作用域[NetCoroutineScope.launch]之前执行
     * 函数内部所有的异常都不会被抛出, 也不会终止作用域执行
     *
     * @param breakError 读取缓存成功后不再处理错误信息
     * @param breakLoading 读取缓存成功后结束加载动画
     * @param block 该作用域内的所有异常都算缓存读取失败, 不会吐司和打印任何错误
     *
     * 这里指的读取缓存也可以替换为其他任务, 比如读取数据库或者其他接口数据
     */
    fun preview(
        breakError: Boolean = false,
        breakLoading: Boolean = true,
        block: suspend CoroutineScope.() -> Unit
    ): AndroidScope {
        this.previewBreakError = breakError
        this.previewBreakLoading = breakLoading
        this.preview = block
        return this
    }

    override fun cancel(cause: CancellationException?) {
        Net.cancelGroup(scopeGroup)
        super.cancel(cause)
    }
}