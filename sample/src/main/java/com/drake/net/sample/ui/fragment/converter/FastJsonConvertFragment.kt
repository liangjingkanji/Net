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

package com.drake.net.sample.ui.fragment.converter

import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.sample.converter.FastJsonConverter
import com.drake.net.sample.databinding.FragmentCustomConvertBinding
import com.drake.net.sample.model.Model
import com.drake.net.utils.scopeNetLife


class FastJsonConvertFragment :
    BaseConvertFragment<FragmentCustomConvertBinding>(R.layout.fragment_custom_convert) {

    override fun initView() {
        binding.tvConvertTip.text = """
            1. 阿里巴巴出品的Json解析库
            2. 引入kotlin-reflect库可以支持kotlin默认值
        """.trimIndent()

        scopeNetLife {
            binding.tvFragment.text = Get<Model>("api") {
                converter = FastJsonConverter() // 单例转换器, 此时会忽略全局转换器
            }.await().data.request_method
        }
    }

    override fun initData() {
    }

}
