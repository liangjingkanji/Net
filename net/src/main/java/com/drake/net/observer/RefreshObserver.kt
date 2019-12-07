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

/**
 * 自动结束下拉刷新
 */
abstract class RefreshObserver<M>(
    val refresh: SmartRefreshLayout,
    val loadMore: Boolean = false
) : TryObserver<RefreshObserver<M>, M>() {


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

    override fun onFailed(e: Throwable) {
        super.onFailed(e)
        refresh.finishRefresh(false)
    }

    override fun handleError(e: Throwable) {
        NetConfig.onError(e)
    }

    override fun onFinish() {
        refresh.finishRefresh(true)
    }
}
