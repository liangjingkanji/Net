/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：12/20/19 2:20 PM
 */

package com.drake.net.scope

import android.view.View
import com.drake.net.NetConfig
import com.drake.statelayout.StateLayout
import kotlinx.coroutines.CancellationException

/**
 * 缺省页作用域
 */
class StateCoroutineScope(val state: StateLayout) : NetCoroutineScope() {

    init {
        state.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
            }

            override fun onViewDetachedFromWindow(v: View) {
                cancel()
            }
        })
    }

    override fun start() {
        isReadCache = !state.loaded
        state.trigger()
    }

    override fun readCache(succeed: Boolean) {
        if (succeed) {
            state.showContent()
        }
    }

    override fun catch(e: Throwable) {
        super.catch(e)
        if (!isCacheSucceed) state.showError(e)
    }

    override fun handleError(e: Throwable) {
        NetConfig.onStateError(e, state)
    }

    override fun finally(e: Throwable?) {
        super.finally(e)
        if (e == null || e is CancellationException) {
            state.showContent()
        }
        state.trigger()
    }

}
