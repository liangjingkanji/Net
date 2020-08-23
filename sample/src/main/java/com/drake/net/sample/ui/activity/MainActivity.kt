/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：4/16/20 3:42 PM
 */

package com.drake.net.sample.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.drake.net.sample.R
import com.drake.statusbar.immersiveDark
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 以下代码设置导航, 和框架本身无关无需关心, 请查看[com.drake.net.sample.ui.fragment]内的Fragment
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        immersiveDark(toolbar)
        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(
            nav.findNavController(),
            AppBarConfiguration(nav_view.menu, drawer)
        )
        nav_view.setupWithNavController(nav.findNavController())
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers()
        } else super.onBackPressed()
    }
}









