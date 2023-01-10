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
import com.drake.net.sample.constants.Api
import com.drake.net.sample.converter.GsonConverter
import com.drake.net.sample.databinding.FragmentCustomConvertBinding
import com.drake.net.sample.model.GameModel
import com.drake.net.utils.scopeNetLife


class GsonConvertFragment :
    BaseConvertFragment<FragmentCustomConvertBinding>(R.layout.fragment_custom_convert) {

    override fun initView() {
        binding.tvConvertTip.text = """
            1. Google官方出品
            2. Json解析库Java上的老牌解析库
            3. 不支持Kotlin构造参数默认值
            4. 支持动态解析
        """.trimIndent()

        scopeNetLife {
            binding.tvFragment.text = Get<GameModel>(Api.GAME) {
                converter = GsonConverter() // 单例转换器, 此时会忽略全局转换器, 在Net中可以直接解析List等嵌套泛型数据
            }.await().list[0].name
        }
    }

    override fun initData() {
    }

}
