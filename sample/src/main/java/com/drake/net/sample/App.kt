package com.drake.net.sample

import android.app.Application
import com.drake.net.convert.DefaultConverter
import com.drake.net.initNet
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.squareup.moshi.Moshi
import java.lang.reflect.Type

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initNet("http://localhost.com") {
            converter(object : DefaultConverter() {
                override fun <S> convert(succeed: Type, body: String): S? {
                    return Moshi.Builder().build().adapter<S>(succeed).fromJson(body)
                }
            })
        }

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout -> ClassicsHeader(this) }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> ClassicsFooter(this) }
    }
}