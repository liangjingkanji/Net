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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.drake.net.NetConfig
import io.reactivex.observers.DefaultObserver

/**
 * 自动加载对话框网络请求
 *
 *
 * 开始: 显示对话框
 * 错误: 提示错误信息, 关闭对话框
 * 完全: 关闭对话框
 *
 * @param activity 对话框跟随生命周期的FragmentActivity
 * @param dialog 单例对话框
 * @param cancelable 是否允许用户取消对话框
 */
abstract class DialogObserver<M>(
    var activity: FragmentActivity,
    var dialog: Dialog? = null,
    var cancelable: Boolean = true
) : DefaultObserver<M>(), LifecycleObserver {

    companion object {

        private var defaultDialog: (DialogObserver<*>.(context: FragmentActivity) -> Dialog)? = null

        fun setDefaultDialog(block: (DialogObserver<*>.(context: FragmentActivity) -> Dialog)) {
            defaultDialog = block
        }
    }


    override fun onStart() {

        activity.lifecycle.addObserver(this)

        dialog = when {
            dialog != null -> dialog
            defaultDialog != null -> defaultDialog?.invoke(this, activity)
            else -> {
                val progress = ProgressDialog(activity)
                progress.setMessage("加载中")
                progress
            }
        }

        dialog?.setOnDismissListener { cancel() }
        dialog?.setCancelable(cancelable)
        dialog?.show()
    }

    @OnLifecycleEvent(Event.ON_DESTROY)
    fun dismissDialog() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    /**
     * 关闭进度对话框并提醒错误信息
     *
     * @param e 包括错误信息
     */
    override fun onError(e: Throwable) {
        dismissDialog()
        NetConfig.onError.invoke(e)
    }

    override fun onComplete() {
        dismissDialog()
    }

}
