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
import kotlinx.android.synthetic.main.fragment_exception_trace.*


class ExceptionTraceFragment : Fragment(R.layout.fragment_exception_trace) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scopeNetLife {
            // 这是一个错误的地址, 请查看LogCat的错误信息, 在[Convert]中你也可以进行自定义错误信息打印
            tv_fragment.text = Get<String>("error").await()
        }
    }

}
