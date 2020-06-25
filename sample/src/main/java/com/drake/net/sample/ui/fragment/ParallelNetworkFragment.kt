/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：4/16/20 3:31 PM
 */

package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.drake.net.Get
import com.drake.net.Post
import com.drake.net.Trace
import com.drake.net.sample.R
import com.drake.net.utils.scopeNetLife


class ParallelNetworkFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_parallel_network, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        scopeNetLife {

            // 同时发起三个请求
            val deferred = Get<String>("api")
            val deferred1 = Post<String>("api")
            val deferred2 = Trace<String>("api")

            // 同时接收三个请求数据
            deferred.await()
            deferred1.await()
            deferred2.await()
        }
    }

}
