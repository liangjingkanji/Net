/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：12/20/19 11:51 AM
 */

package com.drake.net.scope

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 * 异步协程作用域
 */
open class AndroidScope() : CoroutineScope {

    /**
     * 使用该构造函数创建对象可以跟随[LifecycleOwner]的生命周期自动取消作用域
     */
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

    private var catch: (AndroidScope.(Throwable) -> Unit)? = null
    private var finally: (AndroidScope.(Throwable?) -> Unit)? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        catch(throwable)
    }

    final override val coroutineContext: CoroutineContext =
        Dispatchers.Main.immediate + exceptionHandler + Job()

    /**
     * 开启一个作用域
     */
    fun launch(
        block: suspend CoroutineScope.() -> Unit
    ): AndroidScope {
        val job: Job = launch(EmptyCoroutineContext, CoroutineStart.DEFAULT, block)
        job.invokeOnCompletion { finally(it) }
        return this
    }

    @CallSuper
    protected open fun catch(e: Throwable) {
        catch?.invoke(this, e) ?: handleError(e)
    }

    @CallSuper
    protected open fun finally(e: Throwable?) {
        finally?.invoke(this, e)
    }

    /**
     * 当作用域内发生异常时回调
     */
    fun catch(block: AndroidScope.(Throwable) -> Unit): AndroidScope {
        this.catch = block
        return this
    }

    /**
     * 无论正常或者异常结束都将最终执行
     * @param block 如果是发生异常[Throwable]为该异常对象, 正常结束为NULL
     * @see launch 只有通过该函数开启的作用域才有效
     */
    fun finally(block: AndroidScope.(Throwable?) -> Unit): AndroidScope {
        this.finally = block
        return this
    }


    /**
     * 错误处理
     * 一般用于被子类重写, 当前默认为打印异常信息到LogCat
     */
    open fun handleError(e: Throwable) {
        e.printStackTrace()
    }

}

