package com.madiwist.twitch.feature_post.presentation.util

import com.madiwist.twitch.core.util.Error

sealed class PostDescriptionError : Error() {
    object FieldEmpty: PostDescriptionError()
}