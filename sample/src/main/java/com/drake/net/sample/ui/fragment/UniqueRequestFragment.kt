package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.drake.net.Post
import com.drake.net.sample.R
import com.drake.net.scope.AndroidScope
import com.drake.net.utils.scopeNetLife
import kotlinx.android.synthetic.main.fragment_unique_request.*

class UniqueRequestFragment : Fragment(R.layout.fragment_unique_request) {

    private var scope: AndroidScope? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        btn_request.setOnClickListener {
            tv_result.text = "请求中"
            scope?.cancel() // 如果存在则取消

            scope = scopeNetLife {
                val result = Post<String>("api").await()
                Log.d("日志", "请求到结果") // 你一直重复点击"发起请求"按钮会发现永远无法拿到请求结果, 因为每次发起新的请求会取消未完成的
                tv_result.text = result
            }
        }
    }
}