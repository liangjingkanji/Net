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
import com.drake.net.R
import kotlinx.coroutines.cancel

@Suppress("unused", "MemberVisibilityCanBePrivate", "NAME_SHADOWING")
class PageCoroutineScope(
    val page: PageRefreshLayout
) : NetCoroutineScope() {

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
        (page.getTag(R.id.cache_succeed) as? Boolean)?.let {
            readCache = false
            cacheSucceed = it
        }
        page.trigger()
    }

    override fun readCache(succeed: Boolean) {
        if (succeed) {
            page.showContent()
        }
        page.setTag(R.id.cache_succeed, succeed)
    }

    override fun catch(e: Throwable) {
        super.catch(e)

        if (cacheSucceed) {
            page.finish(false)
        } else {
            page.showError(e)
        }
    }

    override fun finally(e: Throwable?) {
        super.finally(e)
        if (e == null) {
            page.showContent()
            page.trigger()
        }
    }

    override fun handleError(e: Throwable) {
        if (cacheSucceed) {
            NetConfig.onError(e)
        } else {
            NetConfig.onStateError(e, page)
        }
    }

}