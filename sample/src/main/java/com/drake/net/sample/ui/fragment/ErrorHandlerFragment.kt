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
import android.view.View
import androidx.fragment.app.Fragment
import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.utils.scopeNetLife
import kotlinx.android.synthetic.main.fragment_error_handler.*


class ErrorHandlerFragment : Fragment(R.layout.fragment_error_handler) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scopeNetLife {
            // 该请求是错误的路径会在控制台打印出错误信息
            Get<String>("error").await()
        }.catch {
            // 重写该函数后, 错误不会流到[NetConfig.onError]中的全局错误处理, 在App.kt中可以自定义该全局处理, 同时包含onStateError
            tv_fragment.text = it.message
        }
    }

}
