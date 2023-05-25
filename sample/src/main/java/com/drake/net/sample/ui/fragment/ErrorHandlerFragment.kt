package com.drake.net.sample.ui.fragment

import com.drake.engine.base.EngineFragment
import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentErrorHandlerBinding
import com.drake.net.utils.scopeNetLife


class ErrorHandlerFragment :
    EngineFragment<FragmentErrorHandlerBinding>(R.layout.fragment_error_handler) {

    override fun initView() {
        scopeNetLife {
            // 该请求是错误的路径会在控制台打印出错误信息
            Get<String>("error").await()
        }.catch {
            // 重写该函数后, 错误不会流到[NetConfig.onError]中的全局错误处理, 在App.kt中可以自定义该全局处理, 同时包含onStateError
            binding.tvFragment.text = it.message
        }
    }

    override fun initData() {
    }

}
