package com.drake.net.callback

import android.view.View
import androidx.annotation.MainThread
import com.drake.net.Net
import com.drake.net.NetConfig
import com.drake.net.interfaces.NetCallback
import com.drake.net.request.group
import com.drake.statelayout.StateLayout
import okhttp3.Call
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.CancellationException

/**
 * 配合SateLayout布局使用
 * 会在加载成功/错误时显示对应的缺省页
 * 会在页面被销毁时自动取消网络请求
 */
abstract class StateCallback<T>(val state: StateLayout) : NetCallback<T>() {

    /** 网络请求开始 */
    @MainThread
    override fun onStart(request: Request) {
        super.onStart(request)
        state.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
            }

            override fun onViewDetachedFromWindow(v: View) {
                Net.cancelGroup(request.group)
            }
        })
    }

    /**
     * 网络请求失败, 异步线程
     * @param e 请求过程中发生错误则
     */
    override fun onFailure(call: Call, e: IOException) {
        state.showError(e)
        super.onFailure(call, e)
    }

    /**
     * 网络请求完成
     * @param e 请求过程中发生错误则
     */
    @MainThread
    override fun onError(call: Call, e: IOException) {
        NetConfig.errorHandler.onStateError(e, state)
    }

    /**
     * 网络请求完成
     * @param e 假设请求过程中发生错误则不为null
     */
    @MainThread
    override fun onComplete(call: Call, e: Throwable?) {
        super.onComplete(call, e)
        if (e == null || e is CancellationException) state.showContent()
    }
}