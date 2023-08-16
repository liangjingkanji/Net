package com.drake.net.sample.ui.fragment

import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.drake.engine.base.EngineFragment
import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.sample.constants.Api
import com.drake.net.sample.databinding.FragmentEditDebounceBinding
import com.drake.net.sample.model.GameModel
import com.drake.net.utils.debounce
import com.drake.net.utils.launchIn
import com.drake.net.utils.scope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.distinctUntilChanged

class EditDebounceFragment :
    EngineFragment<FragmentEditDebounceBinding>(R.layout.fragment_edit_debounce) {

    override fun initData() {
    }


    override fun initView() {
        var searchText = ""
        var scope: CoroutineScope? = null

        // 配置列表
        binding.rv.setup {
            addType<GameModel.Data>(R.layout.item_game)
        }

        // 监听分页
        binding.page.onRefresh {
            scope = scope {
                val data = Get<GameModel>(Api.GAME) {
                    param("search", searchText)
                    param("page", index)
                }.await()
                addData(data.list) {
                    itemCount < data.total
                }
            }
        }

        // distinctUntilChanged 表示过滤掉重复结果
        binding.etInput.debounce().distinctUntilChanged().launchIn(this) {
            scope?.cancel() // 发起新的请求前取消旧的请求, 避免旧数据覆盖新数据
            searchText = it
            if (it.isBlank()) {
                binding.rv.models = null
            } else {
                binding.page.showLoading()
            }
        }
    }
}