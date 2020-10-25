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
import com.drake.net.Post
import com.drake.net.sample.R
import com.drake.net.utils.scopeDialog
import com.drake.tooltip.toast
import kotlinx.android.synthetic.main.fragment_config_dialog.*
import kotlinx.coroutines.CancellationException


class ConfigDialogFragment : Fragment(R.layout.fragment_config_dialog) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        scopeDialog {
            tv_fragment.text = Post<String>("dialog") {
                param("u_name", "drake")
                param("pwd", "123456")
            }.await()
        }.finally {
            // 关闭对话框后执行的异常
            if (it is CancellationException) {
                toast("对话框被关闭, 网络请求自动取消") // 这里存在Handler吐司崩溃, 如果不想处理就直接使用我的吐司库 https://github.com/liangjingkanji/Tooltip
            }
        }
    }
}
