package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.drake.net.Net
import com.drake.net.interfaces.NetCallback
import com.drake.net.sample.R
import kotlinx.android.synthetic.main.fragment_fastest.*
import okhttp3.Call

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
        Net.post("api") {
            param("password", "Net123")
        }.enqueue(object : NetCallback<String>() {
            override fun onSuccess(call: Call, data: String) {
                tv_fragment.text = data // onSuccess 属于主线程
            }
        })
    }
}