/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：4/16/20 3:35 PM
 */

package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.drake.net.Get
import com.drake.net.sample.R
import com.drake.net.utils.scope
import kotlinx.android.synthetic.main.fragment_state_layout.*
import kotlinx.coroutines.delay


class StateLayoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_state_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        state.onRefresh {
            scope {
                delay(1000)

                tv_fragment.text = Get<String>("method") {
                    param("userId", "drake")
                }.await()
            }
        }.showLoading()
    }

}
