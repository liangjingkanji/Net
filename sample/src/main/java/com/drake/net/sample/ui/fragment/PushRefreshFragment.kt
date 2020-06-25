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
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.sample.mod.ListModel
import com.drake.net.utils.scope
import kotlinx.android.synthetic.main.fragment_push_refresh.*


class PushRefreshFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_push_refresh, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rv_push.linear().setup {
            addType<String>(R.layout.item_list)
        }

        page.onRefresh {
            scope {
                rv_push.models = Get<ListModel>("list").await().data.list
            }
        }.autoRefresh()
    }

}
