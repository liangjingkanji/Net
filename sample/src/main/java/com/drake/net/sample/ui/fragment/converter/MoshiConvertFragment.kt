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
import com.drake.net.sample.converter.MoshiConverter
import com.drake.net.sample.databinding.FragmentCustomConvertBinding
import com.drake.net.sample.model.HomeBannerModel
import com.drake.net.utils.scopeNetLife


class MoshiConvertFragment :
    BaseConvertFragment<FragmentCustomConvertBinding>(R.layout.fragment_custom_convert) {

    override fun initView() {
        binding.tvConvertTip.text = """
            1. Square出品的JSON解析库
            2. 支持Kotlin构造默认值
            3. 具备注解和反射两种使用方式
            4. 非可选类型反序列化时赋值Null会抛出异常
            5, 不支持动态解析
        """.trimIndent()

        scopeNetLife {
            binding.tvFragment.text = Get<List<HomeBannerModel>>(Api.BANNER) {
                converter = MoshiConverter() // 单例转换器, 此时会忽略全局转换器
            }.await()[0].desc
        }
    }

    override fun initData() {
    }
}
