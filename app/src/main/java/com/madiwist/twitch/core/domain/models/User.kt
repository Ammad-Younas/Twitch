package com.madiwist.twitch.core.domain.models

data class User(
    val userId: String,
    val profilePictureUrl: String? = null,
    val username: String? = null,
    val description: String? = null,
    val followerCount: Int,
    val followingCountL: Int,
    val postCount: Int,
)