package com.drake.net.sample.ui.fragment

import com.drake.engine.base.EngineFragment
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentSwitchDispatcherBinding
import com.drake.net.utils.scopeLife
import com.drake.net.utils.withIO
import com.drake.net.utils.withMain
import kotlinx.coroutines.launch


class SwitchDispatcherFragment :
    EngineFragment<FragmentSwitchDispatcherBinding>(R.layout.fragment_switch_dispatcher) {

    override fun initView() {
        scopeLife {

            // 点击函数名查看更多相关函数
            launch {
                val data = withMain {
                    "异步调度器切换到主线程"
                }
            }

            val data = withIO {
                "主线程切换到IO调度器"
            }
        }
    }

    override fun initData() {
    }

}
