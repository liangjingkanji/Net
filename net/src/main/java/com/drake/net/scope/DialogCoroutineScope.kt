/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：12/20/19 9:20 PM
 */

package com.drake.net.scope

import android.app.Dialog
import android.app.ProgressDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.drake.net.NetConfig
import com.drake.net.NetConfig.defaultDialog
import com.drake.net.R

/**
 * 自动加载对话框网络请求
 *
 *
 * 开始: 显示对话框
 * 错误: 提示错误信息, 关闭对话框
 * 完全: 关闭对话框
 *
 * @param activity 对话框跟随生命周期的FragmentActivity
 * @param dialog 不使用默认的加载对话框而指定对话框
 * @param cancelable 是否允许用户取消对话框
 */
@Suppress("DEPRECATION")
class DialogCoroutineScope(
    val activity: FragmentActivity,
    var dialog: Dialog? = null,
    val cancelable: Boolean = true
) : AndroidScope(), LifecycleObserver {

    init {
        activity.lifecycle.addObserver(this)

        dialog = when {
            dialog != null -> dialog
            defaultDialog != null -> defaultDialog?.invoke(this, activity)
            else -> {
                val progress = ProgressDialog(activity)
                progress.setMessage(activity.getString(R.string.net_dialog_msg))
                progress
            }
        }

        dialog?.setOnDismissListener { }
        dialog?.setCancelable(cancelable)
        dialog?.show()
    }

    override fun handleError(e: Throwable) {
        NetConfig.onError(e)
    }

    override fun finally(e: Throwable?) {
        super.finally(e)
        dismiss()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun dismiss() {
        if (dialog != null && dialog!!.isShowing) {
            dialog?.dismiss()
        }
    }

}