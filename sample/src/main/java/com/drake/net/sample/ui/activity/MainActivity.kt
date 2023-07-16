package com.drake.net.sample.ui.activity

import android.view.Menu
import android.view.MenuItem
import com.drake.engine.base.EngineActivity
import com.drake.net.Get
import com.drake.net.component.Progress
import com.drake.net.interfaces.ProgressListener
import com.drake.net.sample.R
import com.drake.net.sample.databinding.ActivityMainBinding
import com.drake.net.scope.NetCoroutineScope
import com.drake.net.utils.scopeNetLife
import java.io.File

class MainActivity : EngineActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var downloadScope: NetCoroutineScope

    override fun initView() {

        downloadScope = scopeNetLife {

            val file =
                Get<File>("https://r3---sn-i3b7knld.gvt1.com/edgedl/android/studio/install/2022.2.1.20/android-studio-2022.2.1.20-mac_arm.dmg?mh=fu&pl=22&shardbypass=sd&redirect_counter=1&cm2rm=sn-bavcx-hoael7s&req_id=6e6ac2d1c6f9032b&cms_redirect=yes&mip=180.190.115.150&mm=42&mn=sn-i3b7knld&ms=onc&mt=1685464123&mv=m&mvi=3&rmhost=r1---sn-i3b7knld.gvt1.com&smhost=r3---sn-i3b7kn6s.gvt1.com") {
                    setDownloadFileName("android-studio.apk")
                    setDownloadDir(this@MainActivity.filesDir)
                    setDownloadMd5Verify()
                    setDownloadTempFile()
                    addDownloadListener(object : ProgressListener() {
                        override fun onProgress(p: Progress) {
                            binding.seek?.post {
                                val progress = p.progress()
                                binding.seek.progress = progress
                                binding.tvProgress.text =
                                    "download progress: $progress% download speed: ${p.speedSize()}     " +
                                            "\n\nfileSize: ${p.totalSize()}  downloaded: ${p.currentSize()}  remainingSize: ${p.remainSize()}" +
                                            "\n\nelapsedTime: ${p.useTime()}  timeLeft: ${p.remainTime()}"
                            }
                        }
                    })
                }.await()

        }
    }

    override fun initData() {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_download, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cancel -> downloadScope.cancel() // Cancel download
        }
        return super.onOptionsItemSelected(item)
    }

}









