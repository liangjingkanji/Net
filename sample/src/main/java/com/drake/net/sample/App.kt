package com.drake.net.sample

import android.app.Application
import android.util.Log
import com.drake.net.cacheEnabled
import com.drake.net.initNet
import com.drake.statelayout.StateConfig
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // 缺省页初始化
        StateConfig.apply {
            emptyLayout = R.layout.layout_empty
            loadingLayout = R.layout.layout_loading
            errorLayout = R.layout.layout_error
        }

        initNet("http://localhost.com") {

            /*            converter(object : DefaultConvert() {
                            override fun <S> convert(succeed: Type, body: String): S? {
                                return Moshi.Builder().build().adapter<S>(succeed).fromJson(body)
                            }

                        })*/

            cacheEnabled()

            addInterceptor { chain ->

                val request = chain.request()
                Log.d("日志", "(App.kt:37)    url = ${request.url()}")
                chain.proceed(request)
            }
        }

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            ClassicsHeader(
                context
            )
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            ClassicsFooter(
                context
            )
        }

    }
}