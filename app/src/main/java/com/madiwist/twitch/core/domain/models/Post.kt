package com.madiwist.twitch.core.domain.models

data class Post(
    val username: String?,
    val imageUrl: String?,
    val timestamp: Long?,
    val description: String?,
    val likeCount: Int?,
    val commentCount: Int?,
)