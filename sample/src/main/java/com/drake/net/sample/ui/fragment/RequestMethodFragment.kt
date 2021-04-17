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

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.drake.net.*
import com.drake.net.sample.R
import com.drake.net.utils.scopeNetLife
import kotlinx.android.synthetic.main.fragment_async_task.*


class RequestMethodFragment : Fragment(R.layout.fragment_request_method) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
    }

    private fun GET() {
        scopeNetLife {
            tv_fragment.text = Get<String>("api").await()
        }
    }

    private fun POST() {
        scopeNetLife {
            tv_fragment.text = Post<String>("api").await()
        }
    }

    private fun HEAD() {
        scopeNetLife {
            tv_fragment.text = Head<String>("api").await()
        }
    }

    private fun PUT() {
        scopeNetLife {
            tv_fragment.text = Put<String>("api").await()
        }
    }

    private fun PATCH() {
        scopeNetLife {
            tv_fragment.text = Patch<String>("api").await()
        }
    }

    private fun DELETE() {
        scopeNetLife {
            tv_fragment.text = Delete<String>("api").await()
        }
    }

    private fun TRACE() {
        scopeNetLife {
            tv_fragment.text = Trace<String>("api").await()
        }
    }

    private fun OPTIONS() {
        scopeNetLife {
            tv_fragment.text = Options<String>("api").await()
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
            // tv_fragment.text = Post<String>("api") {
            //     json(JSONObject().run {
            //         put("name", name)
            //         put("age", age)
            //         put("measurements", JSONArray(measurements))
            //     })
            // }.await()

            // 创建JSON
            tv_fragment.text = Post<String>("api") {
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
            R.id.put -> PUT()
            R.id.patch -> PATCH()
            R.id.delete -> DELETE()
            R.id.trace -> TRACE()
            R.id.options -> OPTIONS()
            R.id.json -> JSON()
        }
        return super.onOptionsItemSelected(item)
    }

}
