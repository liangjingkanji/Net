package com.drake.net.sample.ui.fragment

import com.drake.engine.base.EngineFragment
import com.drake.net.Get
import com.drake.net.Post
import com.drake.net.Trace
import com.drake.net.sample.R
import com.drake.net.sample.constants.Api
import com.drake.net.sample.databinding.FragmentParallelNetworkBinding
import com.drake.net.utils.scopeNetLife


class ParallelNetworkFragment :
    EngineFragment<FragmentParallelNetworkBinding>(R.layout.fragment_parallel_network) {

    override fun initView() {
        scopeNetLife {

            // 同时发起三个请求
            val deferred = Get<String>(Api.TEST)
            val deferred1 = Post<String>(Api.TEST)
            val deferred2 = Trace<String>(Api.TEST)

            // 同时接收三个请求数据
            deferred.await()
            deferred1.await()
            deferred2.await()
        }
    }

    override fun initData() {
    }

}
