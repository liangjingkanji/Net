package com.drake.net.utils

import android.annotation.SuppressLint
import android.widget.Toast
import com.drake.net.NetConfig

object TipUtils {

    private var toast: Toast? = null

    /**
     * 重复显示不会覆盖
     * 可以在子线程显示
     */
    @SuppressLint("ShowToast")
    @JvmStatic
    fun toast(message: String?) {
        message ?: return
        runMain {
            toast?.cancel()
            toast = Toast.makeText(NetConfig.app, message, Toast.LENGTH_SHORT)
            toast?.show()
        }
    }
}