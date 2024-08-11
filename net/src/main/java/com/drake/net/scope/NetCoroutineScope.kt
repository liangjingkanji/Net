/*
 * MIT License
 *
 * Copyright (c) 2023 劉強東 https://github.com/liangjingkanji
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.drake.net.scope

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.drake.net.Net
import com.drake.net.NetConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
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
        val catch = catch
        if (catch != null) {
            catch.invoke(this@NetCoroutineScope, e)
        } else if (!previewBreakError) {
            handleError(e)
        }
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