package com.drake.net.sample.ui.fragment

import com.drake.engine.base.EngineFragment
import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.sample.constants.Api
import com.drake.net.sample.databinding.FragmentInterceptorBinding
import com.drake.net.utils.scopeNetLife


class InterceptorFragment :
    EngineFragment<FragmentInterceptorBinding>(R.layout.fragment_interceptor) {

    override fun initView() {
        scopeNetLife {
            binding.tvFragment.text = Get<String>(Api.TEST) {
                // 拦截器只支持全局, 无法单例, 请查看[com.drake.net.sample.interceptor.NetInterceptor]
            }.await()
        }
    }

    override fun initData() {
    }

}
