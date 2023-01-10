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

import com.drake.engine.base.EngineFragment
import com.drake.net.Get
import com.drake.net.Post
import com.drake.net.Trace
import com.drake.net.sample.R
import com.drake.net.sample.constants.Api
import com.drake.net.sample.databinding.FragmentParallelNetworkBinding
import com.drake.net.utils.scopeNetLife


class ParallelNetworkFragment :
    EngineFragment<FragmentParallelNetworkBinding>(R.layout.fragment_parallel_network) {

    override fun initView() {
        scopeNetLife {

            // 同时发起三个请求
            val deferred = Get<String>(Api.TEST)
            val deferred1 = Post<String>(Api.TEST)
            val deferred2 = Trace<String>(Api.TEST)

            // 同时接收三个请求数据
            deferred.await()
            deferred1.await()
            deferred2.await()
        }
    }

    override fun initData() {
    }

}
