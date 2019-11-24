/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

package com.drake.net.observer

import androidx.lifecycle.*
import com.drake.net.NetConfig
import io.reactivex.observers.DisposableObserver


/**
 * 自动显示错误信息
 */
abstract class NetObserver<M>() : DisposableObserver<M>(), LifecycleObserver {

    private var error: (NetObserver<M>.(e: Throwable) -> Unit)? = null

    /**
     * 跟随生命周期
     * @param lifecycle 默认是销毁时取消订阅
     */
    constructor(
        lifecycleOwner: LifecycleOwner?,
        lifecycle: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
    ) : this() {
        lifecycleOwner?.lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == lifecycle) {
                    cancel()
                }
            }
        })
    }

    abstract override fun onNext(it: M)

    /**
     * 关闭进度对话框并提醒错误信息
     *
     * @param e 包括错误信息
     */
    override fun onError(e: Throwable) {
        error?.invoke(this, e) ?: handleError(e)
    }

    fun handleError(e: Throwable) {
        NetConfig.onError(e)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun cancel() {
        dispose()
    }

    override fun onComplete() {
    }

    fun error(block: (NetObserver<M>.(e: Throwable) -> Unit)?): NetObserver<M> {
        error = block
        return this
    }

}
