package com.drake.net.sample.ui.fragment

import com.drake.engine.base.EngineFragment
import com.drake.net.Post
import com.drake.net.sample.R
import com.drake.net.sample.constants.Api
import com.drake.net.sample.databinding.FragmentAutoDialogBinding
import com.drake.net.utils.scopeDialog
import com.drake.tooltip.toast
import kotlinx.coroutines.CancellationException


class AutoDialogFragment :
    EngineFragment<FragmentAutoDialogBinding>(R.layout.fragment_auto_dialog) {

    override fun initView() {
        scopeDialog {
            binding.tvFragment.text = Post<String>(Api.DELAY) {
                param("username", "你的账号")
                param("password", "123456")
            }.await()
        }.finally {
            // 关闭对话框后执行的异常
            if (it is CancellationException) {
                toast("对话框被关闭, 网络请求自动取消") // 这里存在Handler吐司崩溃, 如果不想处理就直接使用我的吐司库 https://github.com/liangjingkanji/Tooltip
            }
        }
    }

    override fun initData() {
    }
}
