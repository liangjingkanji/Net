package com.drake.net.sample.ui.fragment

import androidx.fragment.app.viewModels
import com.drake.engine.base.EngineFragment
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentViewModelRequestBinding
import com.drake.net.sample.utils.HttpUtils
import com.drake.net.sample.vm.UserViewModel
import com.drake.net.utils.scopeNetLife

class ViewModelRequestFragment :
    EngineFragment<FragmentViewModelRequestBinding>(R.layout.fragment_view_model_request) {

    private val userViewModel: UserViewModel by viewModels() // 创建ViewModel

    override fun initView() {

        // 直接将用户信息绑定到视图上
        binding.lifecycleOwner = this
        binding.m = userViewModel

        // 动作开始拉取服务器数据
        binding.btnFetchUserinfo.setOnClickListener {
            userViewModel.fetchUserInfo()
        }
    }

    override fun initData() {

        scopeNetLife {
            val configAsync = HttpUtils.getConfigAsync(this)
            // 经常使用的请求可以封装函数
            val userInfo = HttpUtils.getUser()
            configAsync.await() // 实际上在getUser之前就发起请求, 此处只是等待结果, 这就是并发请求
        }
    }
}