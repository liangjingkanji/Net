package com.drake.net.sample.ui.fragment.converter

import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.sample.constants.Api
import com.drake.net.sample.converter.MoshiConverter
import com.drake.net.sample.databinding.FragmentCustomConvertBinding
import com.drake.net.sample.model.GameModel
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
            binding.tvFragment.text = Get<GameModel>(Api.GAME) {
                converter = MoshiConverter() // 单例转换器, 此时会忽略全局转换器
            }.await().list[0].name
        }
    }

    override fun initData() {
    }
}
