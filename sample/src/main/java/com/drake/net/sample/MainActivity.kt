package com.drake.net.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.drake.net.post

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        post<Model>("") {
            param("key", "value")
        }
    }
}
