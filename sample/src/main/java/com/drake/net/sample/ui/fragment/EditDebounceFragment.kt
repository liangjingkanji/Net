package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.utils.debounce
import com.drake.net.utils.listen
import com.drake.net.utils.scopeNetLife
import kotlinx.android.synthetic.main.fragment_edit_debounce.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.json.JSONObject

class EditDebounceFragment : Fragment(R.layout.fragment_edit_debounce) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var scope: CoroutineScope? = null

        et_input.debounce().listen(this) {
            scope?.cancel() // 发起新的请求前取消旧的请求, 避免旧数据覆盖新数据
            scope = scopeNetLife { // 保存旧的请求到一个变量中
                tv_request_content.text = "请求中"
                val data = Get<String>("http://api.k780.com/?app=life.time&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json",
                                       absolutePath = true).await()
                tv_request_content.text = JSONObject(data).getJSONObject("result").getString("datetime_2")
            }
        }
    }
}