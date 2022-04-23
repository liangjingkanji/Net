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
import com.drake.net.okhttp.setConverter
import com.drake.net.okhttp.setDebug
import com.drake.net.okhttp.setDialogFactory
import com.drake.net.okhttp.setRequestInterceptor
import com.drake.net.sample.BR
import com.drake.net.sample.BuildConfig
import com.drake.net.sample.R
import com.drake.net.sample.converter.GsonConverter
import com.drake.net.sample.interfaces.MyRequestInterceptor
import com.drake.statelayout.StateConfig
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import okhttp3.Cache
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        NetConfig.initialize("http://43.128.31.195/", this) {

            // 超时设置
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)

            // 本框架支持Http缓存协议和强制缓存模式
            cache(Cache(cacheDir, 1024 * 1024 * 128)) // 缓存设置, 当超过maxSize最大值会根据最近最少使用算法清除缓存来限制缓存大小

            setDebug(BuildConfig.DEBUG) // LogCat是否输出异常日志, 异常日志可以快速定位网络请求错误

            addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG)) // 添加日志记录拦截器
            setRequestInterceptor(MyRequestInterceptor()) // 添加请求拦截器, 可配置全局/动态参数
            setConverter(GsonConverter()) // 数据转换器
            setDialogFactory { // 全局加载对话框
                ProgressDialog(it).apply {
                    setMessage("我是全局自定义的加载对话框...")
                }
            }
        }

        initializeThirdPart()
    }

    /** 初始化第三方依赖库库 */
    private fun initializeThirdPart() {

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

