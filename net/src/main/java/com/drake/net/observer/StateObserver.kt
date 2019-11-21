/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

package com.drake.net.observer

import android.app.Activity
import android.view.View
import android.view.View.OnAttachStateChangeListener
import androidx.fragment.app.Fragment
import com.drake.net.NetConfig
import com.drake.statelayout.StateLayout
import com.drake.statelayout.state
import io.reactivex.observers.DefaultObserver

/**
 * 自动显示多状态布局
 */
abstract class StateObserver<M> : DefaultObserver<M> {

    var stateLayout: StateLayout

    constructor(view: View) {
        stateLayout = view.state()
    }

    constructor(activity: Activity) {
        stateLayout = activity.state()
    }

    constructor(fragment: Fragment) {
        stateLayout = fragment.state()
    }

    constructor(stateLayout: StateLayout) {
        this.stateLayout = stateLayout
    }

    fun showEmpty() {
        stateLayout.showEmpty()
        cancel()
    }

    public override fun onStart() {
        stateLayout.showLoading()
        stateLayout.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
            }

            override fun onViewDetachedFromWindow(v: View) {
                cancel()
            }
        })
    }

    abstract override fun onNext(it: M)

    override fun onError(e: Throwable) {
        stateLayout.showError()
        NetConfig.onStateError(e, stateLayout)
    }

    override fun onComplete() {
        stateLayout.showContent()
    }
}
