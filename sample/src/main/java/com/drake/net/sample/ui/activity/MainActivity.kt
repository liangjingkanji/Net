package com.drake.net.sample.ui.activity

import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.drake.engine.base.EngineActivity
import com.drake.net.sample.R
import com.drake.net.sample.databinding.ActivityMainBinding
import com.drake.statusbar.immersive

/**
 * 以下代码设置导航, 和框架本身无关无需关心, 请查看[com.drake.net.sample.ui.fragment]内的Fragment
 */
class MainActivity : EngineActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun initView() {
        immersive(binding.toolbar, true)
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav)

        Glide.with(this)
            .load("https://avatars.githubusercontent.com/u/21078112?v=4")
            .circleCrop()
            .into(binding.drawerNav.getHeaderView(0).findViewById(R.id.iv))

        binding.toolbar.setupWithNavController(
            navController,
            AppBarConfiguration(binding.drawerNav.menu, binding.drawer)
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbar.subtitle =
                (destination as FragmentNavigator.Destination).className.substringAfterLast('.')
        }
        binding.drawerNav.setupWithNavController(navController)
    }

    override fun initData() {
    }

    override fun onBackPressed() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)) binding.drawer.closeDrawers() else super.onBackPressed()
    }
}









