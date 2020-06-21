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
import com.drake.net.Post
import com.drake.net.sample.R
import com.drake.net.utils.scopeNetLife
import com.yanzhenjie.kalle.FormBody
import kotlinx.android.synthetic.main.fragment_upload_file.*


class UploadFileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_upload_file, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        scopeNetLife {
            val fileDir = requireContext().cacheDir.path


            Post<String>("download/img", fileDir) {

                val form = FormBody.newBuilder().build().onProgress { origin, progress ->
                    seek.progress = progress // 进度条
                    // 格式化显示单位
                    val downloadSize =
                        android.text.format.Formatter.formatFileSize(requireContext(), 23)
                    val downloadSpeed =
                        android.text.format.Formatter.formatFileSize(requireContext(), 23)

                    // 显示下载信息
                    tv_progress.text = "上传进度: $progress% 已下载: $downloadSize 下载速度: $downloadSpeed"
                }

            }.await()
        }

    }

}
