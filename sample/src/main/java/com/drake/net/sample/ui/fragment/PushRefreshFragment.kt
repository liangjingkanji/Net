package com.drake.net.sample.ui.fragment

import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.drake.engine.base.EngineFragment
import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.sample.constants.Api
import com.drake.net.sample.databinding.FragmentPushRefreshBinding
import com.drake.net.sample.model.GameModel
import com.drake.net.utils.scope

/** 本页面已禁用上拉加载(添加xml属性app:srlEnableLoadMore="false"), 只允许下拉刷新 */
class PushRefreshFragment :
    EngineFragment<FragmentPushRefreshBinding>(R.layout.fragment_push_refresh) {

    override fun initView() {
        binding.rv.linear().setup {
            addType<GameModel.Data>(R.layout.item_game)
        }

        binding.page.onRefresh {
            scope {
                binding.rv.models = Get<GameModel>(Api.GAME).await().list
            }
        // }.autoRefresh() // 首次下拉刷新
        }.showLoading() // 首次加载缺省页
    }

    override fun initData() {
    }

}
