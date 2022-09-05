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

@file:Suppress("FunctionName")

package com.drake.net.sample.ui.fragment

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.drake.engine.base.EngineFragment
import com.drake.net.*
import com.drake.net.sample.R
import com.drake.net.sample.constants.Api
import com.drake.net.sample.databinding.FragmentRequestMethodBinding
import com.drake.net.utils.scopeNetLife


class RequestMethodFragment :
    EngineFragment<FragmentRequestMethodBinding>(R.layout.fragment_request_method) {

    override fun initView() {
        setHasOptionsMenu(true)
    }

    override fun initData() {
    }

    private fun GET() {
        scopeNetLife {
            binding.tvFragment.text = Get<String>(Api.BANNER).await()
        }
    }

    private fun POST() {
        scopeNetLife {
            binding.tvFragment.text = Post<String>(Api.BANNER).await()
        }
    }

    private fun HEAD() {
        scopeNetLife {
            binding.tvFragment.text = Head<String>(Api.BANNER).await()
        }
    }

    private fun PUT() {
        scopeNetLife {
            binding.tvFragment.text = Put<String>(Api.BANNER).await()
        }
    }

    private fun PATCH() {
        scopeNetLife {
            binding.tvFragment.text = Patch<String>(Api.BANNER).await()
        }
    }

    private fun DELETE() {
        scopeNetLife {
            binding.tvFragment.text = Delete<String>(Api.BANNER).await()
        }
    }

    private fun TRACE() {
        scopeNetLife {
            binding.tvFragment.text = Trace<String>(Api.BANNER).await()
        }
    }

    private fun OPTIONS() {
        scopeNetLife {
            binding.tvFragment.text = Options<String>(Api.BANNER).await()
        }
    }


    /**
     * 请求参数为JSON
     */
    private fun JSON() {
        val name = "金城武"
        val age = 29
        val measurements = listOf(100, 100, 100)

        scopeNetLife {

            // 创建JSONObject对象
            // binding.tvFragment.text = Post<String>(Api.BANNER) {
            //     json(JSONObject().run {
            //         put("name", name)
            //         put("age", age)
            //         put("measurements", JSONArray(measurements))
            //     })
            // }.await()

            // 创建JSON
            binding.tvFragment.text = Post<String>(Api.BANNER) {
                json("name" to name, "age" to age, "measurements" to measurements) // 同时支持Map集合
            }.await()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_request_method, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.get -> GET()
            R.id.post -> POST()
            R.id.head -> HEAD()
            R.id.trace -> TRACE()
            R.id.options -> OPTIONS()
            R.id.delete -> DELETE()
            R.id.put -> PUT()
            R.id.patch -> PATCH()
            R.id.json -> JSON()
        }
        return super.onOptionsItemSelected(item)
    }

}
