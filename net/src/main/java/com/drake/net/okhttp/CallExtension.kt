package com.drake.net.okhttp

import com.drake.net.NetConfig
import okhttp3.Call
import java.lang.ref.WeakReference

/**
 * Call附着到Net上
 */
fun Call.attachToNet() {
    NetConfig.netCalls.add(WeakReference(this))
}

/**
 * Call从Net上分离释放引用
 */
fun Call.detachFromNet() {
    val iterator = NetConfig.netCalls.iterator()
    while (iterator.hasNext()) {
        if (iterator.next().get() == this) {
            iterator.remove()
            return
        }
    }
}