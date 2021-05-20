package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.drake.net.Net
import com.drake.net.sample.R
import kotlinx.android.synthetic.main.fragment_fastest.*

class EnqueueRequestFragment : Fragment(R.layout.fragment_enqueue_request) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Net同样支持OkHttp的队列任务

        // Net.post("api").enqueue(object : Callback {
        //     override fun onFailure(call: Call, e: IOException) {
        //     }
        //
        //     override fun onResponse(call: Call, response: Response) {
        //     }
        // })

        // NetCallback支持数据转换
        // Net.post("api") {
        //     param("password", "Net123")
        // }.enqueue(object : NetCallback<String>() {
        //     override fun onSuccess(call: Call, result: String) {
        //         tv_fragment.text = result // onSuccess 属于主线程
        //     }
        // })

        // 简化版本的队列请求
        Net.post("api").onResult<String> {

            getOrNull()?.let { // 如果成功就不为Null
                Log.d("日志", "请求成功")
                tv_fragment.text = it
            }

            exceptionOrNull()?.apply {
                Log.d("日志", "请求失败")
                printStackTrace() // 如果发生错误就不为Null
            }

            Log.d("日志", "完成请求")
        }
    }
}