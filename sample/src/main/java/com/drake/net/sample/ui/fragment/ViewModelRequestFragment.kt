package com.drake.net.sample.ui.fragment

import androidx.fragment.app.viewModels
import com.drake.engine.base.EngineFragment
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentViewModelRequestBinding
import com.drake.net.sample.vm.UserViewModel

class ViewModelRequestFragment :
    EngineFragment<FragmentViewModelRequestBinding>(R.layout.fragment_view_model_request) {

    private val userViewModel: UserViewModel by viewModels() // 创建ViewModel

    override fun initView() {
        binding.lifecycleOwner = this
        binding.m = userViewModel

        // 动作开始拉取服务器数据
        binding.btnFetchUserinfo.setOnClickListener {
            userViewModel.fetchUserInfo()
        }
    }

    override fun initData() {
    }
}