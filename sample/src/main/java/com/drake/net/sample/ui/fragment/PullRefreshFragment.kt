package com.drake.net.sample.ui.fragment

import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.engine.base.EngineFragment
import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.sample.constants.Api
import com.drake.net.sample.databinding.FragmentPullRefreshBinding
import com.drake.net.sample.model.GameModel
import com.drake.net.utils.scope


class PullRefreshFragment :
    EngineFragment<FragmentPullRefreshBinding>(R.layout.fragment_pull_refresh) {

    override fun initView() {
        binding.rv.linear().setup {
            addType<GameModel.Data>(R.layout.item_pull_list)
        }

        binding.page.onRefresh {
            scope {
                val response = Get<GameModel>(String.format(Api.GAME, index)).await()
                addData(response.list) {
                    itemCount < response.total
                }
            }
        }.autoRefresh()
    }

    override fun initData() {
    }

}
