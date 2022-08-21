package com.drake.net.internal

import android.content.Context
import androidx.startup.Initializer
import com.drake.net.NetConfig

/**
 * 使用AppStartup默认初始化[NetConfig.app], 仅应用主进程下有效
 * 在其他进程使用Net请手动在Application中初始化[NetConfig.initialize]
 */
internal class NetInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        NetConfig.app = context
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}