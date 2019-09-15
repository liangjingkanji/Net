package com.drake.net.sample

import android.app.Application
import com.drake.net.setDefaultNetConfig

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        setDefaultNetConfig("http://192.168.1.1")
    }
}