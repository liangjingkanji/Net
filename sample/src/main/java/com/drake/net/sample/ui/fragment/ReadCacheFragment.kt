/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：4/16/20 3:27 PM
 */

package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.drake.net.Get
import com.drake.net.Post
import com.drake.net.sample.R
import com.drake.net.utils.scopeNetLife
import com.yanzhenjie.kalle.simple.cache.CacheMode
import kotlinx.android.synthetic.main.fragment_read_cache.*


class ReadCacheFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_read_cache, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        scopeNetLife {
            // 然后执行这里(网络请求)
            tv_fragment.text = Post<String>("api", cache = CacheMode.NETWORK_YES_THEN_WRITE_CACHE).await()
            Log.d("日志", "网络请求")
        }.cache {
            // 先执行这里(仅读缓存), 任何异常都视为读取缓存失败
            tv_fragment.text = Get<String>("api", cache = CacheMode.READ_CACHE).await()
            Log.d("日志", "读取缓存")
        }
    }

}
