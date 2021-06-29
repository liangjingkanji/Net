package com.drake.net.sample.ui.fragment

import com.drake.engine.base.EngineFragment
import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentEditDebounceBinding
import com.drake.net.utils.debounce
import com.drake.net.utils.listen
import com.drake.net.utils.scopeNetLife
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.json.JSONObject

class EditDebounceFragment :
    EngineFragment<FragmentEditDebounceBinding>(R.layout.fragment_edit_debounce) {

    override fun initData() {
    }

    override fun initView() {
        var scope: CoroutineScope? = null

        binding.etInput.debounce().listen(this) {
            scope?.cancel() // 发起新的请求前取消旧的请求, 避免旧数据覆盖新数据
            scope = scopeNetLife { // 保存旧的请求到一个变量中
                binding.tvFragment.text = "请求中"
                val data =
                    Get<String>("http://api.k780.com/?app=life.time&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json").await()
                binding.tvFragment.text =
                    JSONObject(data).getJSONObject("result").getString("datetime_2")
            }
        }
    }
}