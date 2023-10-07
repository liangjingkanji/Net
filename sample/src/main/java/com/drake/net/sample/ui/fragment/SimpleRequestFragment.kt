@file:Suppress("FunctionName")

package com.drake.net.sample.ui.fragment

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.drake.engine.base.EngineFragment
import com.drake.net.*
import com.drake.net.sample.R
import com.drake.net.sample.constants.Api
import com.drake.net.sample.databinding.FragmentSimpleRequestBinding
import com.drake.net.utils.scopeNetLife


class SimpleRequestFragment :
    EngineFragment<FragmentSimpleRequestBinding>(R.layout.fragment_simple_request) {

    override fun initView() {
        setHasOptionsMenu(true)
    }

    override fun initData() {
    }

    private fun GET() {
        scopeNetLife {
            binding.tvFragment.text = Get<String>(Api.TEXT).await()
        }
    }

    private fun POST() {
        scopeNetLife {
            binding.tvFragment.text = Post<String>(Api.TEXT).await()
        }
    }

    private fun HEAD() {
        scopeNetLife {
            binding.tvFragment.text = Head<String>(Api.TEXT).await()
        }
    }

    private fun PUT() {
        scopeNetLife {
            binding.tvFragment.text = Put<String>(Api.TEXT).await()
        }
    }

    private fun PATCH() {
        scopeNetLife {
            binding.tvFragment.text = Patch<String>(Api.TEXT).await()
        }
    }

    private fun DELETE() {
        scopeNetLife {
            binding.tvFragment.text = Delete<String>(Api.TEXT).await()
        }
    }

    private fun TRACE() {
        scopeNetLife {
            binding.tvFragment.text = Trace<String>(Api.TEXT).await()
        }
    }

    private fun OPTIONS() {
        scopeNetLife {
            binding.tvFragment.text = Options<String>(Api.TEXT).await()
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
            binding.tvFragment.text = Post<String>(Api.TEXT) {
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
