/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：12/20/19 9:26 PM
 */

package com.drake.net.scope

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.drake.net.NetConfig
import com.yanzhenjie.kalle.Canceler
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.coroutines.EmptyCoroutineContext


/**
 * 自动显示网络错误信息协程作用域
 */
@Suppress("unused", "MemberVisibilityCanBePrivate", "NAME_SHADOWING")
open class NetCoroutineScope() : AndroidScope() {

    protected var isReadCache = true
    protected var onCache: (suspend CoroutineScope.() -> Unit)? = null

    protected var isCacheSucceed = false
        get() = if (onCache != null) field else false

    protected var error = true
        get() = if (isCacheSucceed) field else true

    var animate: Boolean = false

    constructor(
        lifecycleOwner: LifecycleOwner,
        lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
    ) : this() {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (lifeEvent == event) cancel()
            }
        })
    }

    override fun launch(
        block: suspend CoroutineScope.() -> Unit
    ): NetCoroutineScope {
        launch(EmptyCoroutineContext) {
            start()
            if (onCache != null && isReadCache) {
                supervisorScope {
                    isCacheSucceed = try {
                        onCache?.invoke(this)
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

    /**
     * 读取缓存回调
     */
    protected open fun readCache(succeed: Boolean) {}


    override fun handleError(e: Throwable) {
        NetConfig.onError(e)
    }

    override fun catch(e: Throwable) {
        catch?.invoke(this, e) ?: if (error) handleError(e)
    }

    /**
     * 该函数一般用于缓存读取
     * 只在第一次启动作用域时回调
     * 该函数在作用域[launch]之前执行
     * 函数内部所有的异常都不会被抛出, 也不会终止作用域执行
     *
     * @param error 是否在缓存读取成功但网络请求错误时吐司错误信息
     * @param animate 是否在缓存成功后依然显示加载动画
     * @param onCache 该作用域内的所有异常都算缓存读取失败, 不会吐司和打印任何错误
     */
    fun cache(
            error: Boolean = false,
            animate: Boolean = false,
            onCache: suspend CoroutineScope.() -> Unit
             ): AndroidScope {
        this.animate = animate
        this.error = error
        this.onCache = onCache
        return this
    }

    override fun cancel(cause: CancellationException?) {
        Canceler.cancel(uid)
        super.cancel(cause)
    }
}