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

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.drake.net.sample.R
import com.drake.net.utils.scopeLife
import com.drake.net.utils.withIO
import com.drake.net.utils.withMain
import kotlinx.coroutines.launch


class SwitchDispatcherFragment : Fragment(R.layout.fragment_switch_dispatcher) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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

}
