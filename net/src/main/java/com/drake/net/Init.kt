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

package com.drake.net

import com.drake.net.okhttp.toNetOkhttp
import okhttp3.OkHttpClient


/**
 * 初始化框架
 * @param host 请求url的主机名
 * @param config 进行配置网络请求
 *
 * 如果想要自动解析数据模型请配置转换器, 可以继承或者参考默认转换器
 *
 * @see com.drake.net.convert.JSONConvert
 */
@Deprecated("函数迁移", replaceWith = ReplaceWith("NetConfig.init(host, config)"), DeprecationLevel.ERROR)
fun initNet(host: String = "", config: OkHttpClient.Builder.() -> Unit = {}) {
    NetConfig.host = host
    val builder = OkHttpClient.Builder()
    builder.config()
    NetConfig.okHttpClient = builder.toNetOkhttp().build()
}

@Deprecated("函数迁移", replaceWith = ReplaceWith("NetConfig.init(host, config)"), DeprecationLevel.ERROR)
fun initNet(host: String = "", config: OkHttpClient.Builder) {
    NetConfig.host = host
    NetConfig.okHttpClient = config.toNetOkhttp().build()
}