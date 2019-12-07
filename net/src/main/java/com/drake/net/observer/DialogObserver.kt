/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：9/16/19 12:54 AM
 */

@file:Suppress("DEPRECATION")

package com.drake.net.observer

import android.app.Dialog
import android.app.ProgressDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.drake.net.NetConfig
import com.drake.net.NetConfig.defaultDialog
import com.drake.net.R
import io.reactivex.disposables.Disposable

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
abstract class DialogObserver<M>(
    val activity: FragmentActivity?,
    var dialog: Dialog? = null,
    val cancelable: Boolean = true
) : TryObserver<DialogObserver<M>, M>(), LifecycleObserver {

    private var error: (DialogObserver<M>.(e: Throwable) -> Unit)? = null

    constructor(
        fragment: Fragment?,
        dialog: Dialog? = null,
        cancelable: Boolean = true
    ) : this(fragment?.activity, dialog, cancelable)


    override fun onStart(d: Disposable) {
        activity ?: return
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

        dialog?.setOnDismissListener { dispose() }
        dialog?.setCancelable(cancelable)
        dialog?.show()
    }

    @OnLifecycleEvent(Event.ON_DESTROY)
    fun dismiss() {
        if (dialog != null && dialog!!.isShowing) {
            dialog?.dismiss()
        }
    }


    /**
     * 关闭进度对话框并提醒错误信息
     *
     * @param e 包括错误信息
     */
    override fun onFailed(e: Throwable) {
        super.onFailed(e)
        dismiss()
    }

    override fun handleError(e: Throwable) {
        NetConfig.onError(e)
    }

    override fun onFinish() {
        dismiss()
    }

}
