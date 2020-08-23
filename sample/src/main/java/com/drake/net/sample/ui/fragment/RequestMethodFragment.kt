/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：4/16/20 3:24 PM
 */

@file:Suppress("FunctionName")

package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.drake.net.*
import com.drake.net.sample.R
import com.drake.net.utils.scopeNetLife
import kotlinx.android.synthetic.main.fragment_async_task.*


class RequestMethodFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_request_method, container, false)
    }

    private fun GET() {
        scopeNetLife {
            tv_fragment.text = Get<String>("api").await()
        }
    }

    private fun POST() {
        scopeNetLife {
            tv_fragment.text = Post<String>("api").await()
        }
    }

    private fun HEAD() {
        scopeNetLife {
            tv_fragment.text = Head<String>("api").await()
        }
    }

    private fun PUT() {
        scopeNetLife {
            tv_fragment.text = Put<String>("api").await()
        }
    }

    private fun PATCH() {
        scopeNetLife {
            tv_fragment.text = Patch<String>("api").await()
        }
    }

    private fun DELETE() {
        scopeNetLife {
            tv_fragment.text = Delete<String>("api").await()
        }
    }

    private fun TRACE() {
        scopeNetLife {
            tv_fragment.text = Trace<String>("api").await()
        }
    }

    private fun OPTIONS() {
        scopeNetLife {
            tv_fragment.text = Options<String>("api").await()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_request_method, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.get -> GET()
            R.id.post -> POST()
            R.id.head -> HEAD()
            R.id.put -> PUT()
            R.id.patch -> PATCH()
            R.id.delete -> DELETE()
            R.id.trace -> TRACE()
            R.id.options -> OPTIONS()
        }
        return super.onOptionsItemSelected(item)
    }

}
