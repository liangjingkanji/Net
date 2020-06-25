/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：4/16/20 3:24 PM
 */

package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.drake.net.sample.R
import com.drake.net.utils.scope
import kotlinx.android.synthetic.main.fragment_async_task.*
import kotlinx.coroutines.*

class AsyncTaskFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_async_task, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        scope {
            tv_fragment.text = withContext(Dispatchers.IO) {
                delay(2000)
                "结果"
            }
        }
    }


    /**
     * 抽出异步任务为一个函数
     */
    private suspend fun withDownloadFile() = withContext(Dispatchers.IO) {
        "结果"
    }

    private fun CoroutineScope.asyncDownloadFile() = async {
        "结果"
    }
}
