/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：12/20/19 2:20 PM
 */

package com.drake.net.scope

import android.view.View
import com.drake.net.NetConfig
import com.drake.net.R
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlinx.coroutines.cancel

/**
 * 自动结束下拉刷新 协程作用域
 */
class RefreshCoroutineScope(
    val refresh: SmartRefreshLayout,
    loadMore: Boolean = false
) : NetCoroutineScope() {

    init {
        refresh.setEnableLoadMore(loadMore)
        refresh.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                cancel()
            }
        })
    }

    override fun start() {
        readCache = refresh.getTag(R.id.cache_succeed) as? Boolean ?: true
    }

    override fun readCache(succeed: Boolean) {
        refresh.finishRefresh()
        refresh.setTag(R.id.cache_succeed, false)
        autoOff()
    }

    override fun catch(e: Throwable) {
        super.catch(e)
        refresh.finishRefresh(false)
    }

    override fun finally(e: Throwable?) {
        super.finally(e)
        if (e == null && auto) {
            refresh.finishRefresh(true)
        }
    }

    override fun handleError(e: Throwable) {
        NetConfig.onError(e)
    }
}