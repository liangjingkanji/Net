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
import com.drake.brv.utils.setup
import com.drake.engine.base.EngineFragment
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentPushRefreshBinding
import com.drake.net.utils.scope


class PushRefreshFragment :
    EngineFragment<FragmentPushRefreshBinding>(R.layout.fragment_push_refresh) {

    override fun initView() {
        binding.rv.linear().setup {
            addType<String>(R.layout.item_list)
        }

        binding.page.onRefresh {
            scope {
                // rv_push.models = Get<ListModel>("list").await().data.list
            }
        }.autoRefresh()
    }

    override fun initData() {
    }

}
