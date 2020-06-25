/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：4/17/20 7:38 AM
 */

package com.drake.net.sample.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.drake.net.DownloadImage
import com.drake.net.NetConfig
import com.drake.net.sample.R
import com.drake.net.utils.scopeDialog
import kotlinx.android.synthetic.main.fragment_download_image.*


class DownloadImageFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
                             ): View? {

        return inflater.inflate(R.layout.fragment_download_image, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        scopeDialog {
            val file = DownloadImage(NetConfig.host + "download/img", 100, 100).await()
            val uri = Uri.fromFile(file)
            iv_img.setImageURI(uri)
        }
    }
}
