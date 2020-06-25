/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：4/16/20 3:42 PM
 */

package com.drake.net.sample.base

import android.app.Application
import com.drake.net.cacheEnabled
import com.drake.net.initNet
import com.drake.net.sample.BR
import com.drake.net.sample.R
import com.drake.net.sample.callback.JsonConvert
import com.drake.statelayout.StateConfig
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // 全局缺省页配置 [https://github.com/liangjingkanji/StateLayout]
        StateConfig.apply {
            emptyLayout = R.layout.layout_empty
            loadingLayout = R.layout.layout_loading
            errorLayout = R.layout.layout_error
        }

        // Net 网络初始化信息
        // initNet("http://192.168.1.222:80/") {
        initNet("http://182.92.97.186/") {
            converter(JsonConvert()) // 自动解析JSON映射到实体类中, 转换器分为全局和单例, 覆盖生效(拦截器允许多个)
            cacheEnabled()
        }

        // 初始化SmartRefreshLayout, 这是自动下拉刷新和上拉加载采用的第三方库  [https://github.com/scwang90/SmartRefreshLayout/tree/master] V2版本
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            MaterialHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }

        com.drake.brv.BindingAdapter.modelId = BR.m

    }
}