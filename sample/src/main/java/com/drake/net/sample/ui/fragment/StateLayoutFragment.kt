package com.drake.net.sample.ui.fragment

import com.drake.engine.base.EngineFragment
import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.sample.constants.Api
import com.drake.net.sample.databinding.FragmentStateLayoutBinding
import com.drake.net.utils.scope


class StateLayoutFragment :
    EngineFragment<FragmentStateLayoutBinding>(R.layout.fragment_state_layout) {

    override fun initData() {
    }

    override fun initView() {
        binding.state.onRefresh {
            scope {
                binding.tvFragment.text = Get<String>(Api.TEST).await()
            }
        }.showLoading()
    }

}
