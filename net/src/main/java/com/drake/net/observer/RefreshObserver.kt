/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

package com.drake.net.observer

import android.view.View
import android.view.View.OnAttachStateChangeListener
import com.drake.net.NetConfig
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import io.reactivex.observers.DisposableObserver

/**
 * 自动结束下拉刷新
 */
abstract class RefreshObserver<M>(
    val refresh: SmartRefreshLayout,
    val loadMore: Boolean = false
) : DisposableObserver<M>() {

    private var error: (RefreshObserver<M>.(e: Throwable) -> Unit)? = null

    init {
        refresh.setEnableLoadMore(loadMore)
        refresh.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                dispose()
            }
        })
    }

    abstract override fun onNext(it: M)

    override fun onError(e: Throwable) {
        refresh.finishRefresh(false)
        error?.invoke(this, e) ?: handleError(e)
    }

    fun handleError(e: Throwable) {
        NetConfig.onError(e)
    }

    override fun onComplete() {
        refresh.finishRefresh(true)
    }

    fun error(block: (RefreshObserver<M>.(e: Throwable) -> Unit)?): RefreshObserver<M> {
        error = block
        return this
    }
}
