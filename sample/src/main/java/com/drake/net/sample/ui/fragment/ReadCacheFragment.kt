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
import com.drake.net.Post
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentReadCacheBinding
import com.drake.net.utils.scopeNetLife


class ReadCacheFragment : EngineFragment<FragmentReadCacheBinding>(R.layout.fragment_read_cache) {

    override fun initView() {
        scopeNetLife {
            // 然后执行这里(网络请求)
            binding.tvFragment.text =
                Post<String>("api").await()
            Log.d("日志", "网络请求")
        }.preview {
            // 先执行这里(仅读缓存), 任何异常都视为读取缓存失败
            binding.tvFragment.text = Get<String>("api").await()
            Log.d("日志", "读取缓存")
        }
    }

    override fun initData() {
    }

}
