/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：12/20/19 2:20 PM
 */

package com.drake.net.scope

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.drake.net.NetConfig
import com.drake.net.R
import com.drake.statelayout.StateLayout
import com.drake.statelayout.state
import kotlinx.coroutines.cancel

/**
 * 缺省页作用域
 */
class StateCoroutineScope(val state: StateLayout) : NetCoroutineScope() {


    constructor(view: View) : this(view.state())

    constructor(activity: Activity) : this(activity.state())

    constructor(fragment: Fragment) : this(fragment.state())

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
        (state.getTag(R.id.cache_succeed) as? Boolean)?.let {
            readCache = false
            cacheSucceed = it
        }
        state.trigger()
    }

    override fun readCache(succeed: Boolean) {
        if (succeed) {
            state.showContent()
        }
        state.setTag(R.id.cache_succeed, succeed)
    }

    override fun catch(e: Throwable) {
        super.catch(e)
        if (!cacheSucceed) {
            state.showError(e)
        }
    }

    override fun handleError(e: Throwable) {
        if (cacheSucceed) {
            super.handleError(e)
        } else {
            NetConfig.onStateError(e, state)
        }
    }

    override fun finally(e: Throwable?) {
        super.finally(e)
        if (e == null) {
            state.showContent()
            state.trigger()
        }
    }

}
