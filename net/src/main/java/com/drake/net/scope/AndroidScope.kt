/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：12/20/19 11:51 AM
 */

package com.drake.net.scope

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*

/**
 * 异步协程作用域
 */
@Suppress("unused", "MemberVisibilityCanBePrivate", "NAME_SHADOWING")
open class AndroidScope() {

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

    protected var catch: (AndroidScope.(Throwable) -> Unit)? = null
    protected var finally: (AndroidScope.(Throwable?) -> Unit)? = null
    protected var auto = true

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        catch(throwable)
    }

    protected val scope
        get() = CoroutineScope(Dispatchers.Main + exceptionHandler + SupervisorJob())


    open fun launch(
        block: suspend CoroutineScope.() -> Unit
    ): AndroidScope {
        start()
        scope.launch(block = block).invokeOnCompletion { finally(it) }
        return this
    }

    protected open fun start() {

    }

    protected open fun catch(e: Throwable) {
        catch?.invoke(this, e) ?: handleError(e)
    }

    protected open fun finally(e: Throwable?) {
        finally?.invoke(this, e)
    }

    /**
     * 当作用域内发生异常时回调
     */
    open fun catch(block: AndroidScope.(Throwable) -> Unit = {}): AndroidScope {
        this.catch = block
        return this
    }

    /**
     * 无论正常或者异常结束都将最终执行
     */
    open fun finally(block: AndroidScope.(Throwable?) -> Unit = {}): AndroidScope {
        this.finally = block
        return this
    }


    /**
     * 错误处理
     */
    open fun handleError(e: Throwable) {
        e.printStackTrace()
    }

    fun cancel(cause: CancellationException? = null) {
        scope.cancel(cause)
    }

    fun cancel(message: String, cause: Throwable? = null) {
        scope.cancel(message, cause)
    }

    fun autoOff() {
        auto = false
    }

}

