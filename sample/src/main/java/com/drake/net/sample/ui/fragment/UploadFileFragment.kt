/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：4/17/20 7:38 AM
 */

package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.drake.net.sample.R


class UploadFileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_upload_file, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /*   scopeNetLife {
               val fileDir = requireContext().cacheDir.path

               Post<String>("download/img", fileDir) {

                   // 下载进度回调 (普通接口或者上传进度也可以监听)
                   onProgress { progress, byteCount, speed ->

                       seek.progress = progress // 进度条
                       // 格式化显示单位
                       val downloadSize = Formatter.formatFileSize(requireContext(), byteCount)
                       val downloadSpeed = Formatter.formatFileSize(requireContext(), speed)

                       // 显示下载信息
                       tv_progress.text = "上传进度: $progress% 已下载: $downloadSize 下载速度: $downloadSpeed"
                   }
               }.await()
           }*/

    }

}
