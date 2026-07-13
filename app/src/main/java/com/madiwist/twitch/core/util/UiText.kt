package com.madiwist.twitch.core.util

import androidx.annotation.StringRes
import com.madiwist.twitch.R

sealed class UiText {
    data class DynamicString(val value: String): UiText()
    data class StringResource(@param:StringRes val id: Int): UiText()

    companion object {
        fun unknownError(): UiText {
            return StringResource(R.string.unknown_error)
        }
    }
}