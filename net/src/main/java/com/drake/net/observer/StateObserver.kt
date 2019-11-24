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
import io.reactivex.observers.DisposableObserver

/**
 * 自动显示多状态布局
 */
abstract class StateObserver<M> : DisposableObserver<M> {

    val state: StateLayout

    private var error: (StateObserver<M>.(e: Throwable) -> Unit)? = null

    constructor(view: View) {
        state = view.state()
    }

    constructor(activity: Activity) {
        state = activity.state()
    }

    constructor(fragment: Fragment) {
        state = fragment.state()
    }

    constructor(stateLayout: StateLayout) {
        this.state = stateLayout
    }

    public override fun onStart() {
        state.showLoading()
        state.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
            }

            override fun onViewDetachedFromWindow(v: View) {
                dispose()
            }
        })
    }

    abstract override fun onNext(it: M)

    override fun onError(e: Throwable) {
        state.showError()
        error?.invoke(this, e) ?: handleError(e)
    }

    fun handleError(e: Throwable) {
        NetConfig.onStateError(e, state)
    }

    fun error(block: (StateObserver<M>.(e: Throwable) -> Unit)?): StateObserver<M> {
        error = block
        return this
    }

    override fun onComplete() {
        state.showContent()
    }

    /**
     * 显示空缺省页
     * 此操作会导致观察者取消订阅
     */
    fun showEmpty() {
        state.showEmpty()
        dispose()
    }
}
