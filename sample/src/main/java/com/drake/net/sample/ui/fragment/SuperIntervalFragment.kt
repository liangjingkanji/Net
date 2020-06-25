/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：4/16/20 3:36 PM
 */

package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.drake.net.sample.R
import com.drake.net.time.Interval
import kotlinx.android.synthetic.main.fragment_super_interval.*
import java.util.concurrent.TimeUnit


class SuperIntervalFragment : Fragment() {

    lateinit var interval: Interval

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
                             ): View? {

        return inflater.inflate(R.layout.fragment_super_interval, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initToolbar()

        interval = Interval(1, 1, TimeUnit.SECONDS, 5).life(this) // 自定义计数器个数的轮循器, 当[start]]比[end]值大, 且end不等于-1时, 即为倒计时
        // interval = Interval(0, 1, TimeUnit.SECONDS) // 每秒回调一次, 不会自动结束

        interval.subscribe {
            tv_fragment.text = it.toString()
        }.finish {
            tv_fragment.text = "计时完成"
        }.start()
    }


    private fun initToolbar() {
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.menu_interval)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.start -> interval.start()
                R.id.pause -> interval.pause()
                R.id.resume -> interval.resume()
                R.id.reset -> interval.reset()
                R.id.switch_interval -> interval.switch()
                R.id.stop -> interval.stop()
            }
            true
        }
    }

}
