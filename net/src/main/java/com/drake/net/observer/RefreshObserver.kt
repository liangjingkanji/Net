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
import io.reactivex.observers.DefaultObserver

/**
 * 自动结束下拉刷新
 */
abstract class RefreshObserver<M>(
    val refreshLayout: SmartRefreshLayout,
    val enableLoadMore: Boolean = false
) : DefaultObserver<M>() {


    init {
        refreshLayout.setEnableLoadMore(enableLoadMore)
        refreshLayout.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                cancel()
            }
        })
    }

    override fun onError(e: Throwable) {
        refreshLayout.finishRefresh(false)
        NetConfig.onError(e)
    }

    override fun onComplete() {
        refreshLayout.finishRefresh(true)
    }
}
