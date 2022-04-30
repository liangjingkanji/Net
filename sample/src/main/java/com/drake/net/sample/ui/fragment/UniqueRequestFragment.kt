package com.drake.net.sample.ui.fragment

import android.util.Log
import com.drake.engine.base.EngineFragment
import com.drake.net.Post
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentUniqueRequestBinding
import com.drake.net.scope.AndroidScope
import com.drake.net.utils.scopeNetLife

class UniqueRequestFragment :
    EngineFragment<FragmentUniqueRequestBinding>(R.layout.fragment_unique_request) {

    private var scope: AndroidScope? = null

    override fun initView() {
        binding.btnRequest.setOnClickListener {
            binding.tvResult.text = "请求中"
            scope?.cancel() // 如果存在则取消

            scope = scopeNetLife {
                val result = Post<String>("banner/json").await()
                Log.d("日志", "请求到结果") // 你一直重复点击"发起请求"按钮会发现永远无法拿到请求结果, 因为每次发起新的请求会取消未完成的
                binding.tvResult.text = result
            }
        }
    }

    override fun initData() {
    }
}