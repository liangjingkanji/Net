/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：4/16/20 3:34 PM
 */

package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.drake.net.Post
import com.drake.net.sample.R
import com.drake.net.utils.scopeDialog
import com.drake.tooltip.toast
import kotlinx.android.synthetic.main.fragment_config_dialog.*
import kotlinx.coroutines.CancellationException


class ConfigDialogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_config_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        scopeDialog {
            tv_fragment.text = Post<String>("dialog") {
                param("u_name", "drake")
                param("pwd", "123456")
            }.await()
        }.finally {
            // 关闭对话框后执行的异常
            if (it is CancellationException) {
                toast("对话框被关闭, 网络请求自动取消") // 这里存在Handler吐司崩溃, 如果不想处理就直接使用我的吐司库 https://github.com/liangjingkanji/Tooltip
            }
        }
    }
}
