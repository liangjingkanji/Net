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
            addType<GameModel.Data>(R.layout.item_list)
        }

        binding.page.onRefresh {
            scope {
                binding.rv.models = Get<GameModel>(Api.GAME).await().list
            }
        }.autoRefresh()
    }

    override fun initData() {
    }

}
