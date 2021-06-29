@file:Suppress("MemberVisibilityCanBePrivate")

package com.drake.net.callback

import android.app.Dialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.drake.net.Net
import com.drake.net.NetConfig
import com.drake.net.interfaces.NetCallback
import com.drake.net.request.group
import okhttp3.Call
import okhttp3.Request

/**
 * 会自动在网络请求开始显示加载对话框, 在网络请求时关闭对话框.
 *
 * @property activity 加载对话框所在的Activity
 * @property dialog 单例加载对话框. 会覆盖全部加载对话框
 * @property cancelable 加载对话框是否可以手动取消. 注意对话框取消网络请求也会被取消
 */
abstract class DialogCallback<T> constructor(
    val activity: FragmentActivity,
    var dialog: Dialog? = null,
    val cancelable: Boolean = true,
) : NetCallback<T>(), LifecycleObserver {

    override fun onStart(request: Request) {
        super.onStart(request)
        activity.lifecycle.addObserver(this)
        activity.runOnUiThread {
            dialog = when {
                dialog != null -> dialog
                else -> NetConfig.dialogFactory.onCreate(activity)
            }
            dialog?.setOnDismissListener { Net.cancelGroup(request.group) }
            dialog?.setCancelable(cancelable)
            dialog?.show()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun dismiss() {
        if (dialog != null && dialog!!.isShowing) {
            dialog?.dismiss()
        }
    }

    override fun onComplete(call: Call, e: Throwable?) {
        dismiss()
        super.onComplete(call, e)
    }
}