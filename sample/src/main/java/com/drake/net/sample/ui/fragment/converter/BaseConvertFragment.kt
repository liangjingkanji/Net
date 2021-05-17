package com.drake.net.sample.ui.fragment.converter

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.drake.net.sample.R

open class BaseConvertFragment(@LayoutRes contentLayoutId: Int = 0) : Fragment(contentLayoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_converter, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.onNavDestinationSelected(findNavController())
        return true
    }
}
