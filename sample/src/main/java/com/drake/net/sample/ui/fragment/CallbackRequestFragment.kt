package com.drake.net.sample.ui.fragment

import com.drake.engine.base.EngineFragment
import com.drake.net.Net
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentCallbackRequestBinding
import com.drake.net.utils.runMain
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class CallbackRequestFragment :
    EngineFragment<FragmentCallbackRequestBinding>(R.layout.fragment_callback_request) {

    override fun initData() {
    }

    override fun initView() {
        // Net同样支持OkHttp原始的队列任务
        Net.post("banner/json").enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                // 此处为子线程
                val body = response.body?.string() ?: "无数据"
                runMain {
                    // 此处为主线程
                    binding.tvFragment.text = body
                }
            }
        })
    }
}