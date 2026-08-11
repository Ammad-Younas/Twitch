package com.madiwist.twitch.core.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import com.madiwist.twitch.R
import java.io.File
import java.io.FileOutputStream

fun Context.saveBitmapToCache(bitmap: Bitmap, name: String): Uri {
    val file = File(cacheDir, "${name}_${System.currentTimeMillis()}.jpg")
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
    return file.toUri()
}


fun Context.sendSharePostIntent(postId: String) {
    val postUrl = "http://madiwist.com/$postId"
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            getString(
                R.string.share_intent_text,
                postUrl
            )
        )
    }
    startActivity(
        Intent.createChooser(
            intent,
            getString(R.string.share_post)
        )
    )
}