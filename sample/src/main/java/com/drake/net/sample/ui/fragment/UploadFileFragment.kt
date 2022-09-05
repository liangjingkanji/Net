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

import com.drake.engine.base.EngineFragment
import com.drake.net.Post
import com.drake.net.component.Progress
import com.drake.net.interfaces.ProgressListener
import com.drake.net.sample.R
import com.drake.net.sample.constants.Api
import com.drake.net.sample.databinding.FragmentUploadFileBinding
import com.drake.net.utils.scopeNetLife
import okio.buffer
import okio.sink
import okio.source
import java.io.File


class UploadFileFragment :
    EngineFragment<FragmentUploadFileBinding>(R.layout.fragment_upload_file) {

    override fun initView() {
        scopeNetLife {
            Post<String>(Api.UPLOAD) {
                param("file", assetsFile())
                addUploadListener(object : ProgressListener() {
                    override fun onProgress(p: Progress) {
                        binding.seek.post {
                            binding.seek.progress = p.progress()
                            binding.tvProgress.text =
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

    override fun initData() {
    }
}
