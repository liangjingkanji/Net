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
import android.view.View
import androidx.fragment.app.Fragment
import com.drake.net.Post
import com.drake.net.component.Progress
import com.drake.net.interfaces.ProgressListener
import com.drake.net.sample.R
import com.drake.net.utils.scopeNetLife
import kotlinx.android.synthetic.main.fragment_upload_file.*
import okio.buffer
import okio.sink
import okio.source
import java.io.File


class UploadFileFragment : Fragment(R.layout.fragment_upload_file) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scopeNetLife {
            Post<String>("https://download.sublimetext.com/Sublime%20Text%20Build%203211.dmg") {
                param("file", assetsFile())
                addUploadListener(object : ProgressListener() {
                    override fun onProgress(p: Progress) {
                        seek.post {
                            seek.progress = p.progress()
                            tv_progress.text =
                                "上传进度: ${p.progress()}% 上传速度: ${p.speedSize()}     " +
                                        "\n\n文件大小: ${p.totalSize()}  已上传: ${p.currentSize()}  剩余大小: ${p.remainSize()}" +
                                        "\n\n已使用时间: ${p.useTime()}  剩余时间: ${p.remainTime()}"
                        }
                    }
                })
            }.await()
        }
    }

    private fun assetsFile(): File {
        val fileName = "upload_file.jpg"
        val inputStream = resources.assets.open(fileName)
        val file = File(requireContext().filesDir.path, fileName)
        inputStream.source().buffer().use {
            it.readAll(file.sink())
        }
        return file
    }
}
