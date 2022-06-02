package com.drake.net.interfaces

import android.app.Dialog
import android.app.ProgressDialog
import androidx.activity.ComponentActivity
import com.drake.net.R

fun interface NetDialogFactory {

    /**
     * 构建并返回Dialog. 当使用 scopeDialog 作用域时将会自动显示该对话框且作用域完成后关闭对话框
     *
     * @param activity 请求发生所在的[ComponentActivity]
     */
    fun onCreate(activity: ComponentActivity): Dialog

    companion object DEFAULT : NetDialogFactory {
        override fun onCreate(activity: ComponentActivity): Dialog {
            val progress = ProgressDialog(activity)
            progress.setMessage(activity.getString(R.string.net_dialog_msg))
            return progress
        }
    }
}