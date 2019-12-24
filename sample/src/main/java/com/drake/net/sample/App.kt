package com.drake.net.sample

import android.app.Application
import com.drake.net.initNet
import com.drake.statelayout.StateConfig
import com.yanzhenjie.kalle.simple.SimpleUrlRequest
import com.yanzhenjie.kalle.simple.cache.DiskCacheStore

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

            addInterceptor {


                val request = it.request()

                if (request is SimpleUrlRequest) {
                }

                it.proceed(request)
            }

            val cacheStore = DiskCacheStore.newBuilder(cacheDir.absolutePath)
                .password("cache")
                .build()

            cacheStore(cacheStore)
        }

    }
}