/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：1/18/20 4:41 PM
 */

package com.drake.net.sample.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.drake.net.get
import com.drake.net.sample.R
import com.drake.net.utils.scope
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        content.onRefresh {

            scope {
                val data = get<String>("https://github.com/liangjingkanji")
                textView.text = data.await()
            }

        }.showLoading()

    }
}










