/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drake.net.sample.base

import android.app.Application
import android.app.ProgressDialog
import com.drake.brv.BindingAdapter
import com.drake.net.NetConfig
import com.drake.net.interceptor.LogRecordInterceptor
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.okhttp.setConverter
import com.drake.net.okhttp.setDialogFactory
import com.drake.net.okhttp.setLog
import com.drake.net.okhttp.setRequestInterceptor
import com.drake.net.request.BaseRequest
import com.drake.net.sample.BR
import com.drake.net.sample.BuildConfig
import com.drake.net.sample.R
import com.drake.net.sample.converter.GsonConverter
import com.drake.statelayout.StateConfig
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        NetConfig.initialize("http://43.128.31.195/") {

            // 超时设置
            connectTimeout(2, TimeUnit.MINUTES)
            readTimeout(2, TimeUnit.MINUTES)
            writeTimeout(2, TimeUnit.MINUTES)

            setLog(BuildConfig.DEBUG) // LogCat异常日志
            addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG)) // 添加日志记录器
            setRequestInterceptor(object : RequestInterceptor { // 添加请求拦截器
                override fun interceptor(request: BaseRequest) {
                    request.addHeader("client", "Net")
                    request.setHeader("token", "123456")
                }
            })

            setConverter(GsonConverter()) // 数据转换器

            setDialogFactory { // 全局加载对话框
                ProgressDialog(it).apply {
                    setMessage("我是全局自定义的加载对话框...")
                }
            }
        }

        initDependent()
    }

    /**
     * 初始化Net的可选附属库
     */
    private fun initDependent() {

        // 全局缺省页配置 [https://github.com/liangjingkanji/StateLayout]
        StateConfig.apply {
            emptyLayout = R.layout.layout_empty
            loadingLayout = R.layout.layout_loading
            errorLayout = R.layout.layout_error
        }


        // 初始化SmartRefreshLayout, 这是自动下拉刷新和上拉加载采用的第三方库  [https://github.com/scwang90/SmartRefreshLayout/tree/master] V2版本
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            MaterialHeader(context)
        }

        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }

        BindingAdapter.modelId = BR.m
    }
}

