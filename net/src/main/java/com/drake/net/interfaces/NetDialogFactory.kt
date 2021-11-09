package com.drake.net.interfaces

import android.app.Dialog
import android.app.ProgressDialog
import androidx.fragment.app.FragmentActivity
import com.drake.net.R

fun interface NetDialogFactory {

    /**
     * 构建并返回一个Dialog
     * @param activity 请求发生所在的[FragmentActivity]
     */
    fun onCreate(activity: FragmentActivity): Dialog

    companion object DEFAULT : NetDialogFactory {
        override fun onCreate(activity: FragmentActivity): Dialog {
            val progress = ProgressDialog(activity)
            progress.setMessage(activity.getString(R.string.net_dialog_msg))
            return progress
        }
    }
}