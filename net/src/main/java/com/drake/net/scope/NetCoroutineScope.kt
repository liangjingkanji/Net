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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.coroutines.EmptyCoroutineContext


/**
 * 自动显示网络错误信息协程作用域
 */
@Suppress("unused", "MemberVisibilityCanBePrivate", "NAME_SHADOWING")
open class NetCoroutineScope() : AndroidScope() {

    protected var readCache = true
    protected var onCache: (suspend CoroutineScope.() -> Unit)? = null

    protected var cacheSucceed = false
        get() = if (onCache != null) field else false

    protected var error = true
        get() = if (cacheSucceed) field else true


    constructor(
        lifecycleOwner: LifecycleOwner,
        lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
    ) : this() {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (lifeEvent == event) {
                    cancel()
                }
            }
        })
    }

    override fun launch(
        block: suspend CoroutineScope.() -> Unit
    ): NetCoroutineScope {
        launch(EmptyCoroutineContext) {
            start()
            if (onCache != null && readCache) {
                supervisorScope {
                    cacheSucceed = try {
                        onCache?.invoke(this)
                        true
                    } catch (e: Exception) {
                        false
                    }
                    readCache(cacheSucceed)
                }
            }

            block()
        }.invokeOnCompletion {
            finally(it)
        }

        return this
    }

    protected open fun readCache(succeed: Boolean) {

    }

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
     */
    fun cache(
        error: Boolean = false,
        animate: Boolean = true,
        onCache: suspend CoroutineScope.() -> Unit
    ): AndroidScope {
        this.error = error
        this.onCache = onCache
        return this
    }

}