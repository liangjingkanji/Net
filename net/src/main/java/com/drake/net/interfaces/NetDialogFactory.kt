package com.drake.net.interfaces

import android.app.Dialog
import androidx.fragment.app.FragmentActivity
import com.drake.net.NetConfig

fun interface NetDialogFactory {
    fun onCreate(activity: FragmentActivity): Dialog

    companion object DEFAULT : NetDialogFactory {
        override fun onCreate(activity: FragmentActivity): Dialog {
            return NetConfig.onDialog.invoke(activity)
        }
    }
}