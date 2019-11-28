package com.drake.net.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.drake.net.download
import com.drake.net.observer.net

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val netObserver = download(
            "https://cdn.sspai.com/article/ebe361e4-c891-3afd-8680-e4bad609723e.jpg?imageMogr2/quality/95/thumbnail/!2880x620r/gravity/Center/crop/2880x620/interlace/1",
            isAbsolutePath = true
        ).net(this) {
        }.error {
            handleError(it)
        }

        netObserver.dispose() // 立刻取消

    }
}
