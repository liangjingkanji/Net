package com.drake.net.sample.ui.fragment

import com.drake.engine.base.EngineFragment
import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.sample.constants.Api
import com.drake.net.sample.databinding.FragmentEditDebounceBinding
import com.drake.net.utils.debounce
import com.drake.net.utils.launchIn
import com.drake.net.utils.scopeNetLife
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.distinctUntilChanged

class EditDebounceFragment :
    EngineFragment<FragmentEditDebounceBinding>(R.layout.fragment_edit_debounce) {

    override fun initData() {
    }

    override fun initView() {
        var scope: CoroutineScope? = null

        // distinctUntilChanged 表示过滤掉重复结果
        binding.etInput.debounce().distinctUntilChanged().launchIn(this) {
            scope?.cancel() // 发起新的请求前取消旧的请求, 避免旧数据覆盖新数据
            scope = scopeNetLife { // 保存旧的请求到一个变量中
                binding.tvFragment.text = "请求中"
                binding.tvFragment.text = Get<String>(Api.TIME).await()
            }
        }
    }
}