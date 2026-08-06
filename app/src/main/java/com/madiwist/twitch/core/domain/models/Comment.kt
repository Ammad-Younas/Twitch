package com.madiwist.twitch.core.domain.models

data class Comment(
    val commentId: String,
    val username: String,
    val profilePictureUrl: String,
    val timeStamp: String,
    val comment: String,
    val isLiked: Boolean,
    val likeCount: Int
)