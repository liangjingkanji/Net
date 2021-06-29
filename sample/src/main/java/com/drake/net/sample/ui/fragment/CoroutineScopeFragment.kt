/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drake.net.sample.ui.fragment

import androidx.lifecycle.Lifecycle
import com.drake.engine.base.EngineFragment
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentCoroutineScopeBinding
import com.drake.net.utils.scope
import com.drake.net.utils.scopeLife
import com.drake.net.utils.scopeNet
import com.drake.net.utils.scopeNetLife
import kotlinx.coroutines.delay


class CoroutineScopeFragment :
    EngineFragment<FragmentCoroutineScopeBinding>(R.layout.fragment_coroutine_scope) {
    override fun initData() {
        // 其作用域在应用进程销毁时才会被动取消
        scope {

        }

        // 其作用域在Activity或者Fragment销毁(onDestroy)时被动取消 [scopeNetLife]
        scopeLife {
            delay(2000)
            binding.tvFragment.text = "任务结束"
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

    override fun initView() {
    }
}
