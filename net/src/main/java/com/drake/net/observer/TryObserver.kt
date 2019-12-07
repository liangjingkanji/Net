/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：12/6/19 5:38 PM
 */
package com.drake.net.observer

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.Exceptions
import io.reactivex.internal.disposables.DisposableHelper
import java.util.concurrent.atomic.AtomicReference

/**
 * 捕捉所有异常
 */

abstract class TryObserver<T : Observer<M>, M> : AtomicReference<Disposable>(), Observer<M>,
    Disposable {

    private var error: (T.(e: Throwable) -> Unit)? = null

    /**
     * 错误处理回调
     *
     * 只在观察者和被观察者事件发送时存在不同步的情况下生效(一般用于异步处理)
     */
    fun error(block: (T.(e: Throwable) -> Unit)?): T {
        error = block
        @Suppress("UNCHECKED_CAST")
        return this as T
    }

    // <editor-fold desc="异常捕捉">


    protected open fun trySubscribe(d: Disposable) {}

    @Suppress("UNCHECKED_CAST")
    protected open fun tryError(e: Throwable) {
        error?.invoke(this as T, e) ?: handleError(e)
    }

    protected abstract fun tryNext(it: M)

    protected open fun tryComplete() {}

    abstract fun handleError(e: Throwable)

    // </editor-fold>

    override fun onSubscribe(d: Disposable) {
        if (DisposableHelper.setOnce(this, d)) {
            try {
                trySubscribe(d)
            } catch (ex: Throwable) {
                Exceptions.throwIfFatal(ex)
                d.dispose()
                onError(ex)
            }
        }
    }

    override fun onNext(t: M) {
        if (!isDisposed) {
            try {
                tryNext(t)
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                get().dispose()
                onError(e)
            }
        }
    }


    override fun onError(t: Throwable) {
        if (!isDisposed) {
            lazySet(DisposableHelper.DISPOSED)
            try {
                tryError(t)
            } catch (e: Throwable) {
                e.printStackTrace()
                Exceptions.throwIfFatal(e)
            }
        }
    }

    override fun onComplete() {
        if (!isDisposed) {
            lazySet(DisposableHelper.DISPOSED)
            try {
                tryComplete()
            } catch (e: Throwable) {
                e.printStackTrace()
                Exceptions.throwIfFatal(e)
            }
        }
    }

    override fun dispose() {
        DisposableHelper.dispose(this)
    }

    override fun isDisposed(): Boolean {
        return get() === DisposableHelper.DISPOSED
    }

    companion object {
        private const val serialVersionUID = -7251123623727029452L
    }

}