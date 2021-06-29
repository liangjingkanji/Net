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
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentAsyncTaskBinding
import com.drake.net.utils.scope
import kotlinx.coroutines.*

class AsyncTaskFragment : EngineFragment<FragmentAsyncTaskBinding>(R.layout.fragment_async_task) {

    override fun initView() {
        scope {
            binding.tvFragment.text = withContext(Dispatchers.IO) {
                delay(2000)
                "结果"
            }
        }
    }

    override fun initData() {
    }

    /**
     * 抽出异步任务为一个函数
     */
    private suspend fun withDownloadFile() = withContext(Dispatchers.IO) {
        delay(200)
        "结果"
    }

    private fun CoroutineScope.asyncDownloadFile() = async {
        "结果"
    }
}
