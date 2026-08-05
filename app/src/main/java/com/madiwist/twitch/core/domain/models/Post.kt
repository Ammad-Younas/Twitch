package com.madiwist.twitch.core.domain.models

data class Post(
    val id: String?,
    val imageUrl: String?,
    val userId: String?,
    val username: String?,
    val timestamp: Long?,
    val description: String?,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
)