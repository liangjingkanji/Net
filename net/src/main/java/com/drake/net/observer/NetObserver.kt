/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

package com.drake.net.observer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.drake.net.NetConfig
import io.reactivex.observers.DefaultObserver


/**
 * 自动显示错误信息
 */
abstract class NetObserver<M>() : DefaultObserver<M>(), LifecycleObserver {

    constructor(lifecycleOwner: LifecycleOwner?) : this() {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    /**
     * 关闭进度对话框并提醒错误信息
     *
     * @param e 包括错误信息
     */
    override fun onError(e: Throwable) {
        NetConfig.onError.invoke(e)
    }

    override fun onComplete() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cancelRequest() {
        cancel()
    }
}
