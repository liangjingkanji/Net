package com.drake.net.sample.ui.fragment

import android.view.View
import com.drake.engine.base.EngineFragment
import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentTimingRequestBinding
import com.drake.net.utils.scopeNetLife
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import org.json.JSONObject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class TimingRequestFragment : EngineFragment<FragmentTimingRequestBinding>(R.layout.fragment_timing_request) {

    private var scope: CoroutineScope? = null

    override fun initView() {
        binding.v = this
    }

    override fun initData() {
    }

    override fun onClick(v: View) {
        when (v) {
            binding.btnRepeat -> repeatRequest()
            binding.infinityRepeat -> infinityRequest()
            binding.btnCancel -> scope?.cancel()
        }
    }

    /** 重复请求10次 */
    private fun repeatRequest() {
        scope?.cancel()
        scope = scopeNetLife {
            // 每两秒请求一次, 总共执行10次
            repeat(20) {
                delay(1000)
                val data =
                    Get<String>("http://api.k780.com/?app=life.time&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json").await()
                binding.tvContent.text =
                    JSONObject(data).getJSONObject("result").getString("datetime_2")
                // 通过return@repeat可以终止循环
            }
        }
    }

    /** 无限次数请求 */
    private fun infinityRequest() {
        scope?.cancel()
        scope = scopeNetLife {
            // 每两秒请求一次, 总共执行10次
            while (true) {
                delay(1.toDuration(DurationUnit.SECONDS))
                val data =
                    Get<String>("http://api.k780.com/?app=life.time&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json").await()
                binding.tvContent.text =
                    JSONObject(data).getJSONObject("result").getString("datetime_2")
                // 通过break可以终止循环
            }
        }
    }

}