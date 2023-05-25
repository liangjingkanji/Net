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
