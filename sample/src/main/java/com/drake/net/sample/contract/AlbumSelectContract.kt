package com.drake.net.sample.contract

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

class AlbumSelectContract : ActivityResultContract<Unit, AlbumSelectContract.AlbumSelectResult>() {

    override fun createIntent(context: Context, input: Unit): Intent {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        return intent
    }

    class AlbumSelectResult(val code: Int, val uri: Uri?)

    override fun parseResult(resultCode: Int, intent: Intent?): AlbumSelectResult {
        println("AlbumSelectContract >>> selected = ${intent?.data}")
        return AlbumSelectResult(resultCode, intent?.data)
    }
}