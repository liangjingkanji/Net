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
import java.io.IOException

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
            dialog?.setOnDismissListener { Net.cancelGroup(request.group()) }
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

    override fun onComplete(call: Call, e: IOException?) {
        dismiss()
        super.onComplete(call, e)
    }
}