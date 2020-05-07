/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：4/16/20 3:38 PM
 */

package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.sample.callback.GsonConvert
import com.drake.net.sample.mod.Model
import com.drake.net.utils.scopeNetLife


class CustomConvertFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_custom_convert, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        scopeNetLife {
            val model = Get<Model>("") {
                converter(GsonConvert()) // 单例转换器, 此时会忽略全局转换器
            }.await()
        }

    }

}
