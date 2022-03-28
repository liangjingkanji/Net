package com.drake.net.utils

import android.annotation.SuppressLint
import android.widget.Toast
import com.drake.net.NetConfig

object TipUtils {

    private var toast: Toast? = null

    /**
     * 重复显示不会覆盖, 可以在子线程显示
     * 本方法会导致报内存泄露, 这是因为为了避免吐司反复显示导致重叠会长期持有Toast引用来保持单例导致, 可以无视或者自己实现吐司
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