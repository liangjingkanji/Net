package com.drake.net.sample.ui.fragment

import android.app.Activity
import android.net.Uri
import com.drake.engine.base.EngineFragment
import com.drake.net.Post
import com.drake.net.component.Progress
import com.drake.net.interfaces.ProgressListener
import com.drake.net.sample.R
import com.drake.net.sample.constants.Api
import com.drake.net.sample.contract.AlbumSelectContract
import com.drake.net.sample.databinding.FragmentUploadFileBinding
import com.drake.net.sample.utils.RandomFileUtils
import com.drake.net.utils.TipUtils
import com.drake.net.utils.scopeNetLife
import java.io.File


class UploadFileFragment : EngineFragment<FragmentUploadFileBinding>(R.layout.fragment_upload_file) {
    private val albumSelectLauncher = registerForActivityResult(AlbumSelectContract()) {
        when (it.code) {
            Activity.RESULT_CANCELED -> TipUtils.toast("取消图片选择")
            Activity.RESULT_OK -> uploadUri(it.uri)
        }
    }

    override fun initView() {
        binding.btnFile.setOnClickListener {
            uploadFile()
        }
        binding.btnUri.setOnClickListener {
            albumSelectLauncher.launch(null)
        }
    }

    private fun uploadFile() {
        scopeNetLife {
            Post<String>(Api.UPLOAD) {
                param("file", getRandomFile())
                addUploadListener(object : ProgressListener() {
                    override fun onProgress(p: Progress) {
                        binding.seek.post {
                            binding.seek.progress = p.progress()
                            binding.tvProgress.text = "上传进度: ${p.progress()}% 上传速度: ${p.speedSize()}     " + "\n\n文件大小: ${p.totalSize()}  已上传: ${p.currentSize()}  剩余大小: ${p.remainSize()}" + "\n\n已使用时间: ${p.useTime()}  剩余时间: ${p.remainTime()}"
                        }
                    }
                })
            }.await()
        }
    }

    private fun uploadUri(uri: Uri?) {
        scopeNetLife {
            Post<String>(Api.UPLOAD) {
                param("file", uri)
                addUploadListener(object : ProgressListener() {
                    override fun onProgress(p: Progress) {
                        binding.seek.post {
                            binding.seek.progress = p.progress()
                            binding.tvProgress.text = "上传进度: ${p.progress()}% 上传速度: ${p.speedSize()}     " + "\n\n文件大小: ${p.totalSize()}  已上传: ${p.currentSize()}  剩余大小: ${p.remainSize()}" + "\n\n已使用时间: ${p.useTime()}  剩余时间: ${p.remainTime()}"
                        }
                    }
                })
            }.await()
        }
    }

    /** 生成指定大小的随机文件 */
    private fun getRandomFile(): File {
        val file = File(requireContext().filesDir, "uploadFile.apk")
        // 本演示项目的Mock服务不支持太大的文件, 可能会OOM溢出, 实际接口请求不存在
        RandomFileUtils.createRandomFile(file, 30, RandomFileUtils.FileSizeUnit.MB)
        return file
    }

    override fun initData() {
    }
}
