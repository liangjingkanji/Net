package com.drake.net.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.drake.net.get
import com.drake.net.utils.scopeDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        scopeDialog {

            val data = get<String>(
                "https://raw.githubusercontent.com/liangjingkanji/BRV/master/README.md",
                absolutePath = true
            ) {
                cacheKey("wu")
            }

            textView.text = data.await()
        }
    }

}










