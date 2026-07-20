package com.madiwist.twitch.core.domain.util

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns

fun ContentResolver.getFileName(uri: Uri) : String {
    val cursor = query(uri, null, null, null, null)
    val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    cursor?.moveToFirst()
    val fileName = cursor?.getString(nameIndex!!).toString()
    cursor?.close()
    return fileName
}