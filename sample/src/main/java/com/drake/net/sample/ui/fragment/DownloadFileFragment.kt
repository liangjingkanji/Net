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
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.drake.net.Get
import com.drake.net.component.Progress
import com.drake.net.interfaces.ProgressListener
import com.drake.net.sample.R
import com.drake.net.scope.NetCoroutineScope
import com.drake.net.utils.scopeNetLife
import kotlinx.android.synthetic.main.fragment_download_file.*
import java.io.File
import kotlin.time.ExperimentalTime


class DownloadFileFragment : Fragment(R.layout.fragment_download_file) {

    private lateinit var downloadScope: NetCoroutineScope

    @ExperimentalTime
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)


        downloadScope = scopeNetLife {
            val file =
                Get<File>("https://download.sublimetext.com/Sublime%20Text%20Build%203211.dmg") {
                    setDownloadFileName("net.apk")
                    setDownloadDir(requireContext().filesDir)
                    setDownloadMd5Verify()
                    setDownloadTempFile()
                    addDownloadListener(object : ProgressListener() {
                        override fun onProgress(p: Progress) {
                            Log.d("日志", "(DownloadFileFragment.kt:52)    p = ${p}")
                            seek?.post {
                                val progress = p.progress()
                                seek.progress = progress
                                tv_progress.text =
                                    "下载进度: $progress% 下载速度: ${p.speedSize()}     " +
                                            "\n\n文件大小: ${p.totalSize()}  已下载: ${p.currentSize()}  剩余大小: ${p.remainSize()}" +
                                            "\n\n已使用时间: ${p.useTime()}  剩余时间: ${p.remainTime()}"
                            }
                        }
                    })
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
