package com.drake.net.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.drake.net.downloadImg
import com.drake.net.observer.dialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        downloadImg("static-mobile.xbzhaopin.com/xy00000/images_fh/upload/2019/04/1555745799934.png").dialog(
            this
        ) {

        }
    }
}
