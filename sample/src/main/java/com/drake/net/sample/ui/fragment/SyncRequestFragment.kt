package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.drake.net.Net
import com.drake.net.sample.R
import kotlinx.android.synthetic.main.fragment_sync_request.*
import kotlin.concurrent.thread

class SyncRequestFragment : Fragment(R.layout.fragment_sync_request) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        thread {
            val result = Net.post("api").execute<String>() // 网络请求不允许在主线程
            // val result = Net.post("api").toResult<String>().getOrDefault("请求发生错误, 我这是默认值")
            tv_fragment?.post {
                tv_fragment?.text = result  // view要求在主线程更新
            }
        }
    }
}