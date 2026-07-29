package com.madiwist.twitch.core.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream

fun Context.saveBitmapToCache(bitmap: Bitmap, name: String): Uri {
    val file = File(cacheDir, "${name}_${System.currentTimeMillis()}.jpg")
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
    return file.toUri()
}
