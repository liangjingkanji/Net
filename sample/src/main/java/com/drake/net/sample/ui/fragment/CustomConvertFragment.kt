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
import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.sample.callback.GsonConvert
import com.drake.net.sample.mod.Model
import com.drake.net.utils.scopeNetLife
import kotlinx.android.synthetic.main.fragment_custom_convert.*


class CustomConvertFragment : Fragment(R.layout.fragment_custom_convert) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        scopeNetLife {
            tv_fragment.text = Get<Model>("api") {
                converter(GsonConvert()) // 单例转换器, 此时会忽略全局转换器
            }.await().data.request_method
        }

    }

}
