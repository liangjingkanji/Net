/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：4/16/20 3:40 PM
 */

package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.utils.scopeNetLife
import kotlinx.android.synthetic.main.fragment_exception_trace.*


class ExceptionTraceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_exception_trace, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        scopeNetLife {
            // 这是一个错误的地址, 请查看LogCat的错误信息, 在[Convert]中你也可以进行自定义错误信息打印
            tv_fragment.text = Get<String>("").await()
        }
    }

}
