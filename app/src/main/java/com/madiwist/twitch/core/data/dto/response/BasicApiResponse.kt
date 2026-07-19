package com.madiwist.twitch.core.data.dto.response

data class BasicApiResponse<T> (
    val success: Boolean,
    val message: String? = null,
    val data: T? = null
)