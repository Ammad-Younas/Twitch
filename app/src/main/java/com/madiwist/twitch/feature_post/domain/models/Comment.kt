package com.madiwist.twitch.feature_post.domain.models

data class Comment(
    val username: String = "",
    val profilePictureUrl: String = "",
    val comment: String = "",
    val isLiked: Boolean = false,
    val likeCount: Int = 0,
    val commentId: Int = 0,
    val timeStamp: Long = System.currentTimeMillis()
)
