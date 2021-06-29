/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drake.net.sample.ui.fragment

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.drake.engine.base.EngineFragment
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentSuperIntervalBinding
import com.drake.net.time.Interval
import java.util.concurrent.TimeUnit


class SuperIntervalFragment :
    EngineFragment<FragmentSuperIntervalBinding>(R.layout.fragment_super_interval) {

    private lateinit var interval: Interval // 轮循器

    override fun initView() {
        interval = Interval(
            0,
            1,
            TimeUnit.SECONDS,
            10
        ).life(this) // 自定义计数器个数的轮循器, 当[start]]比[end]值大, 且end不等于-1时, 即为倒计时
        // interval = Interval(1, TimeUnit.SECONDS) // 每秒回调一次, 不会自动结束
        interval.subscribe {
            binding.tvFragment.text = it.toString()
        }.finish {
            binding.tvFragment.text = "计时完成"
        }.start()
    }

    override fun initData() {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_interval, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.start -> interval.start()
            R.id.pause -> interval.pause()
            R.id.resume -> interval.resume()
            R.id.reset -> interval.reset()
            R.id.switch_interval -> interval.switch()
            R.id.stop -> interval.stop()
        }
        return super.onOptionsItemSelected(item)
    }

}
