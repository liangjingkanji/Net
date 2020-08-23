/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Net
 * Author：Drake
 * Date：4/17/20 7:38 AM
 */

package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.text.format.Formatter
import android.view.*
import androidx.fragment.app.Fragment
import com.drake.net.Download
import com.drake.net.sample.R
import com.drake.net.scope.NetCoroutineScope
import com.drake.net.utils.scopeNetLife
import kotlinx.android.synthetic.main.fragment_download_file.*


class DownloadFileFragment : Fragment() {

    private lateinit var downloadScope: NetCoroutineScope

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_download_file, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        downloadScope = scopeNetLife {
            Download("download", requireContext().filesDir.path) {
                // 下载进度回调
                onProgress { progress, byteCount, speed ->
                    // 进度条
                    seek.progress = progress

                    // 格式化显示单位
                    val downloadSize = Formatter.formatFileSize(requireContext(), byteCount)
                    val downloadSpeed = Formatter.formatFileSize(requireContext(), speed)

                    // 显示下载信息
                    tv_progress.text = "下载进度: $progress% 已下载: $downloadSize 下载速度: $downloadSpeed"
                }
            }.await()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_download, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cancel -> downloadScope.cancel() // 取消下载
        }
        return super.onOptionsItemSelected(item)
    }
}
