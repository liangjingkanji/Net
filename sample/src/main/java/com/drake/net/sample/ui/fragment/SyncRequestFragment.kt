package com.drake.net.sample.ui.fragment

import com.drake.engine.base.EngineFragment
import com.drake.net.Net
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentSyncRequestBinding
import kotlin.concurrent.thread

class SyncRequestFragment :
    EngineFragment<FragmentSyncRequestBinding>(R.layout.fragment_sync_request) {

    override fun initView() {
        thread {
            val result = Net.post("api").execute<String>() // 网络请求不允许在主线程
            // val result = Net.post("api").toResult<String>().getOrDefault("请求发生错误, 我这是默认值")
            binding.tvFragment?.post {
                binding.tvFragment?.text = result  // view要求在主线程更新
            }
        }
    }

    override fun initData() {
    }
}