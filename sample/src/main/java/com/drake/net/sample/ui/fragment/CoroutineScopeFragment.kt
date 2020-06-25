/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：4/16/20 3:41 PM
 */

package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.drake.net.sample.R
import com.drake.net.utils.scope
import com.drake.net.utils.scopeLife
import com.drake.net.utils.scopeNet
import com.drake.net.utils.scopeNetLife
import kotlinx.android.synthetic.main.fragment_coroutine_scope.*
import kotlinx.coroutines.delay


class CoroutineScopeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
                             ): View? {
        return inflater.inflate(R.layout.fragment_coroutine_scope, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 其作用域在应用进程销毁时才会被动取消
        scope {

        }

        // 其作用域在Activity或者Fragment销毁(onDestroy)时被动取消 [scopeNetLife]
        scopeLife {
            delay(2000)
            tv_fragment.text = "任务结束"
        }

        // 自定义取消跟随的生命周期, 失去焦点时立即取消作用域
        scopeLife(Lifecycle.Event.ON_PAUSE) {

        }

        // 此作用域会捕捉发生的异常, 如果是网络异常会进入网络异常的全局处理函数, 例如自动弹出吐司 [NetConfig.onError]
        scopeNet {

        }

        // 自动网络处理 + 生命周期管理
        scopeNetLife {

        }
    }

}
