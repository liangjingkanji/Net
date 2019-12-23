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
import com.drake.statelayout.StateLayout
import com.drake.statelayout.state
import kotlinx.coroutines.cancel

/**
 * 缺省页协程作用域
 */
class StateCoroutineScope(val state: StateLayout) : AndroidScope() {


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

    override fun catch(e: Throwable) {
        super.catch(e)
        state.showError()
        NetConfig.onStateError(e, state)
    }

    override fun finally(e: Throwable?) {
        if (e == null) {
            state.showContent()
        }
        super.finally(e)
    }

    /**
     * 显示空缺省页
     */
    fun showEmpty() {
        state.showEmpty()
        cancel()
    }

}
