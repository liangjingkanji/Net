package com.drake.net.interfaces

import android.app.Dialog
import androidx.fragment.app.FragmentActivity
import com.drake.net.NetConfig

fun interface NetDialogFactory {

    /**
     * 构建并返回一个Dialog
     * @param activity 请求发生所在的[FragmentActivity]
     */
    fun onCreate(activity: FragmentActivity): Dialog

    companion object DEFAULT : NetDialogFactory {
        override fun onCreate(activity: FragmentActivity): Dialog {
            return NetConfig.onDialog.invoke(activity)
        }
    }
}