package com.madiwist.twitch.core.domain.models

data class Post(
    val id: String?,
    val imageUrl: String?,
    val userId: String?,
    val username: String?,
    val timestamp: Long?,
    val description: String?,
    val likeCount: Int?,
    val commentCount: Int?,
)