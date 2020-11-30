/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.text.format.Formatter
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.drake.net.Download
import com.drake.net.sample.R
import com.drake.net.scope.NetCoroutineScope
import com.drake.net.utils.scopeNetLife
import kotlinx.android.synthetic.main.fragment_download_file.*


class DownloadFileFragment : Fragment(R.layout.fragment_download_file) {

    private lateinit var downloadScope: NetCoroutineScope

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        downloadScope = scopeNetLife {
            val filePath = Download("download", requireContext().filesDir.path) {
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
