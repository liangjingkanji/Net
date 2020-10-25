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
import androidx.fragment.app.Fragment
import com.drake.net.Post
import com.drake.net.sample.R
import com.drake.net.utils.scopeNetLife
import com.yanzhenjie.kalle.FormBody
import kotlinx.android.synthetic.main.fragment_upload_file.*
import java.io.File
import java.io.FileOutputStream


class UploadFileFragment : Fragment(R.layout.fragment_upload_file) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        scopeNetLife {
            Post<String>("upload") {
                val saveFile = getFile()
                val form = FormBody.newBuilder().file("file", saveFile).build()

                form.onProgress { origin, progress ->

                    seek.progress = progress // 进度条
                    // 格式化显示单位
                    val downloadSize =
                        android.text.format.Formatter.formatFileSize(requireContext(), 23)
                    val downloadSpeed =
                        android.text.format.Formatter.formatFileSize(requireContext(), 23)

                    // 显示下载信息
                    tv_progress.text = "上传进度: $progress% 已下载: $downloadSize 下载速度: $downloadSpeed"
                }

                body(form)

            }.await()
        }

    }

    /**
     * @return 上传的文件
     */
    private fun getFile(): File {
        val open = resources.assets.open("upload_file.jpg")
        val saveFile = File("${requireContext().filesDir.path}/upload_file.jpg")
        val buffer = ByteArray(open.available())
        open.read(buffer)
        val fileOutputStream = FileOutputStream(saveFile)
        fileOutputStream.write(buffer)
        open.close()
        fileOutputStream.close()
        return saveFile
    }

}
