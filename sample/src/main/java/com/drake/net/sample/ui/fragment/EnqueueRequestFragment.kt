package com.drake.net.sample.ui.fragment

import android.util.Log
import com.drake.engine.base.EngineFragment
import com.drake.net.Net
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentEnqueueRequestBinding

class EnqueueRequestFragment :
    EngineFragment<FragmentEnqueueRequestBinding>(R.layout.fragment_enqueue_request) {

    override fun initData() {
    }

    override fun initView() {

        // Net同样支持OkHttp的队列任务

        // Net.post("api").enqueue(object : Callback {
        //     override fun onFailure(call: Call, e: IOException) {
        //     }
        //
        //     override fun onResponse(call: Call, response: Response) {
        //     }
        // })

        // NetCallback支持数据转换
        // Net.post("api") {
        //     param("password", "Net123")
        // }.enqueue(object : NetCallback<String>() {
        //     override fun onSuccess(call: Call, result: String) {
        //         binding.tvFragment.text = result // onSuccess 属于主线程
        //     }
        // })

        // 简化版本的队列请求
        Net.post("api").onResult<String> {

            getOrNull()?.let { // 如果成功就不为Null
                Log.d("日志", "请求成功")
                binding.tvFragment.text = it
            }

            exceptionOrNull()?.apply {
                Log.d("日志", "请求失败")
                printStackTrace() // 如果发生错误就不为Null
            }

            Log.d("日志", "完成请求")
        }
    }
}