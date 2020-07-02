/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：12/20/19 2:19 PM
 */

package com.drake.net.scope

import android.view.View
import com.drake.brv.PageRefreshLayout
import com.drake.net.NetConfig
import kotlinx.coroutines.CancellationException

@Suppress("unused", "MemberVisibilityCanBePrivate", "NAME_SHADOWING")
class PageCoroutineScope(val page: PageRefreshLayout) : NetCoroutineScope() {

    val index get() = page.index

    init {
        page.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                cancel()
            }
        })
    }

    override fun start() {
        isReadCache = !page.loaded
        page.trigger()
    }

    override fun readCache(succeed: Boolean) {
        if (succeed && !animate) {
            page.showContent()
        }
        page.loaded = succeed
    }

    override fun catch(e: Throwable) {
        super.catch(e)
        page.showError(e)
    }

    override fun finally(e: Throwable?) {
        super.finally(e)
        if (e == null || e is CancellationException) {
            page.showContent()
        }
        page.trigger()
    }

    override fun handleError(e: Throwable) {
        if (page.loaded || !page.stateEnabled) {
            NetConfig.onError(e)
        } else {
            NetConfig.onStateError(e, page)
        }
    }

}