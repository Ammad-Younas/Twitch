package com.madiwist.twitch.core.util

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatUtil {
    fun timestampToFormatedString(timestamp: Long, pattern: String) : String {
        return SimpleDateFormat(pattern, Locale.getDefault()).run {
            format(timestamp)
        }
    }
}