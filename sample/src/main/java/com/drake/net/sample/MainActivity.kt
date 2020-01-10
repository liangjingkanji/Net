package com.drake.net.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.drake.net.get
import com.drake.net.utils.scope
import com.yanzhenjie.kalle.simple.cache.CacheMode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        content.onRefresh {

            scope {

                Log.d("日志", "(MainActivity.kt:50)    网络")

                val data = get<String>(
                    "https://raw.githubusercontent.com/liangjingkanji/BRV/master/README.md",
                    cache = CacheMode.NETWORK_YES_THEN_WRITE_CACHE,
                    absolutePath = true
                )

                textView.text = data.await()

            }.cache(true) {

                Log.d("日志", "(MainActivity.kt:57)    缓存")

                val data = get<String>(
                    "https://raw.githubusercontent.com/liangjingkanji/BRV/master/README.md",
                    cache = CacheMode.READ_CACHE,
                    absolutePath = true
                )

                textView.text = data.await()
            }

        }.autoRefresh()
    }
}










