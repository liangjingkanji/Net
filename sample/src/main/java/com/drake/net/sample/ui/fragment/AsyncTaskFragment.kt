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
