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

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.drake.net.sample.R
import com.drake.net.time.Interval
import kotlinx.android.synthetic.main.fragment_super_interval.*
import java.util.concurrent.TimeUnit


class SuperIntervalFragment : Fragment() {

    private lateinit var interval: Interval

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_super_interval, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        interval = Interval(
            1,
            1,
            TimeUnit.SECONDS,
            5
        ).life(this) // 自定义计数器个数的轮循器, 当[start]]比[end]值大, 且end不等于-1时, 即为倒计时
        // interval = Interval(1, TimeUnit.SECONDS) // 每秒回调一次, 不会自动结束
        interval.subscribe {
            tv_fragment.text = it.toString()
        }.finish {
            tv_fragment.text = "计时完成"
        }.start()
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
