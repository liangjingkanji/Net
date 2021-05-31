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

import android.os.Bundle
import android.view.View
import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.sample.converter.SerializationConverter
import com.drake.net.sample.model.UserModel
import com.drake.net.utils.scopeNetLife
import kotlinx.android.synthetic.main.fragment_custom_convert.*


class SerializationConvertFragment : BaseConvertFragment(R.layout.fragment_custom_convert) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tv_convert_tip.text = """
            1. kotlin官方出品, 推荐使用 
            2. kotlinx.serialization 是Kotlin上是最完美的序列化工具 
            3. 支持多种反序列化数据类型Pair/枚举/Map...
            4. 多配置选项
            5. 支持动态解析
            6. 支持ProtoBuf/CBOR/JSON等数据
        """.trimIndent()

        scopeNetLife {
            val userList = Get<List<UserModel>>("list-data") {
                // 该转换器直接解析JSON中的data字段, 而非返回的整个JSON字符串
                converter = SerializationConverter() // 单例转换器, 此时会忽略全局转换器
            }.await()

            tv_fragment.text = userList[0].name
        }
    }

}
