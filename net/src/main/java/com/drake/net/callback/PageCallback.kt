package com.drake.net.callback

import android.view.View
import com.drake.brv.PageRefreshLayout
import com.drake.net.Net
import com.drake.net.NetConfig
import com.drake.net.interfaces.NetCallback
import com.drake.net.request.group
import okhttp3.Call
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.CancellationException

/**
 * 配合PageRefreshLayout布局使用
 * 会在加载成功/错误时显示对应的缺省页
 * 会在页面被销毁时自动取消网络请求
 */
abstract class PageCallback<T>(val page: PageRefreshLayout) : NetCallback<T>() {
    override fun onStart(request: Request) {
        super.onStart(request)
        page.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                Net.cancelGroup(request.group)
            }
        })
    }

    override fun onFailure(call: Call, e: IOException) {
        page.showError(e)
        super.onFailure(call, e)
    }

    override fun onError(call: Call, e: IOException) {
        if (page.loaded || !page.stateEnabled) {
            NetConfig.errorHandler.onError(e)
        } else {
            NetConfig.errorHandler.onStateError(e, page)
        }
        super.onError(call, e)
    }

    override fun onComplete(call: Call, e: IOException?) {
        if (e == null || e is CancellationException) {
            page.showContent()
        }
        super.onComplete(call, e)
    }
}