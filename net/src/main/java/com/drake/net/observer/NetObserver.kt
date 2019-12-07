/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

package com.drake.net.observer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.drake.net.NetConfig


/**
 * 自动显示错误信息
 */
abstract class NetObserver<M>() : TryObserver<NetObserver<M>, M>(), LifecycleObserver {

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
                if (event == lifecycle) dispose()
            }
        })
    }

    override fun handleError(e: Throwable) {
        NetConfig.onError(e)
    }
}
