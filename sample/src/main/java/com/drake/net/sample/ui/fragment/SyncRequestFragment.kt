package com.drake.net.sample.ui.fragment

import com.drake.engine.base.EngineFragment
import com.drake.net.Net
import com.drake.net.sample.R
import com.drake.net.sample.constants.Api
import com.drake.net.sample.databinding.FragmentSyncRequestBinding
import kotlin.concurrent.thread

class SyncRequestFragment :
    EngineFragment<FragmentSyncRequestBinding>(R.layout.fragment_sync_request) {

    override fun initView() {
        thread { // 网络请求不允许在主线程
            val result = try {
                Net.post(Api.TEST).execute<String>()
            } catch (e: Exception) { // 同步请求失败会导致崩溃要求捕获异常
                "请求错误 = ${e.message}"
            }

            // val result = Net.post(Api.BANNER).toResult<String>().getOrDefault("请求发生错误, 我这是默认值")

            binding.tvFragment?.post { // 这里用?号是避免界面被销毁依然赋值
                binding.tvFragment?.text = result  // view要求在主线程更新
            }
        }
    }

    override fun initData() {
    }
}