package com.madiwist.twitch.feature_post.domain.models

data class Post(
    val username: String,
    val imageUrl: String,
    val description: String,
    val likeCount: Int,
    val commentCount: Int
)