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

package com.drake.net.sample.ui.fragment

import android.util.Log
import com.drake.engine.base.EngineFragment
import com.drake.net.Get
import com.drake.net.cache.CacheMode
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentReadCacheBinding
import com.drake.net.utils.scopeNetLife


/** 预览缓存. 其实不仅仅是双重加载缓存/网络也可以用于回退请求, 可以执行两次作用域并且忽略preview{}内的所有错误 */
class PreviewCacheFragment : EngineFragment<FragmentReadCacheBinding>(R.layout.fragment_read_cache) {

    override fun initView() {

        // 一般用于秒开首页或者回退加载数据. 我们可以在preview{}只加载缓存. 然后再执行scopeNetLife来请求网络, 做到缓存+网络双重加载的效果

        scopeNetLife {
            // 然后执行这里(网络请求)
            binding.tvFragment.text = Get<String>("api") {
                setCacheMode(CacheMode.WRITE)
            }.await()
            Log.d("日志", "网络请求")
        }.preview(true) {
            // 先执行这里(仅读缓存), 任何异常都视为读取缓存失败
            binding.tvFragment.text = Get<String>("api") {
                setCacheMode(CacheMode.READ)
            }.await()
            Log.d("日志", "读取缓存")
        }
    }

    override fun initData() {
    }

}
