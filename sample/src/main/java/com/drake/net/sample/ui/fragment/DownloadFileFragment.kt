package com.drake.net.sample.ui.fragment

import android.annotation.SuppressLint
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.drake.engine.base.EngineFragment
import com.drake.net.Get
import com.drake.net.component.Progress
import com.drake.net.interfaces.ProgressListener
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentDownloadFileBinding
import com.drake.net.scope.NetCoroutineScope
import com.drake.net.utils.scopeNetLife
import java.io.File


class DownloadFileFragment :
    EngineFragment<FragmentDownloadFileBinding>(R.layout.fragment_download_file) {

    private lateinit var downloadScope: NetCoroutineScope

    @SuppressLint("SetTextI18n")
    override fun initView() {
        setHasOptionsMenu(true)
        downloadScope = scopeNetLife {
            val file =
                Get<File>("http://releases.ubuntu.com/22.04.2/ubuntu-22.04.2-live-server-amd64.iso") {
                    setDownloadFileName("ubuntu-22.04.2-live-server-amd64.iso")
                    setDownloadDir(requireContext().filesDir)
                    setDownloadMd5Verify(false)
                    setDownloadTempFile(true)
                    setDownloadPartial()
                    addDownloadListener(object : ProgressListener() {
                        override fun onProgress(p: Progress) {
                            binding.seek?.post {
                                val progress = p.progress()
                                binding.seek.progress = progress
                                binding.tvProgress.text =
                                    "下载进度: $progress% 下载速度: ${p.speedSize()}/s     " +
                                            "\n\n文件大小: ${p.totalSize()}  已下载: ${p.currentSize()}  剩余大小: ${p.remainSize()}" +
                                            "\n\n已使用时间: ${p.useTime()}  剩余时间: ${p.remainTime()}"
                            }
                        }
                    })
                }.await()

            // 下载完成才会执行此处(所有网络请求使用await都会等待请求完成), 只是监听文件下载完成请不要使用[addDownloadListener]
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

    override fun initData() {
    }
}
