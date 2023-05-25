package com.drake.net.sample.ui.fragment.converter

import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.sample.constants.Api
import com.drake.net.sample.converter.FastJsonConverter
import com.drake.net.sample.databinding.FragmentCustomConvertBinding
import com.drake.net.sample.model.GameModel
import com.drake.net.utils.scopeNetLife


class FastJsonConvertFragment :
    BaseConvertFragment<FragmentCustomConvertBinding>(R.layout.fragment_custom_convert) {

    override fun initView() {
        binding.tvConvertTip.text = """
            1. 阿里巴巴出品的Json解析库
            2. 引入kotlin-reflect库可以支持kotlin默认值
        """.trimIndent()

        scopeNetLife {
            binding.tvFragment.text = Get<GameModel>(Api.GAME) {
                converter = FastJsonConverter() // 单例转换器, 此时会忽略全局转换器
            }.await().list[0].name
        }
    }

    override fun initData() {
    }

}
