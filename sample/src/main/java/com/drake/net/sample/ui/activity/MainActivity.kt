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
import androidx.navigation.ui.setupWithNavController
import com.drake.net.sample.R
import com.drake.statusbar.immersiveDark
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        immersiveDark(toolbar)

        fragment_nav.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            toolbar.title = destination.label // 更新标题
            toolbar.menu.clear() // 清除菜单
        }

        toolbar.setNavigationOnClickListener { drawer.openDrawer(GravityCompat.START) }
        nav.setupWithNavController(fragment_nav.findNavController())
    }

    override fun onSupportNavigateUp(): Boolean {
        return false
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers()
        } else super.onBackPressed()
    }
}









