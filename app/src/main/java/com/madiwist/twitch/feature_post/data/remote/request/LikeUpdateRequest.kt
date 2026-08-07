package com.madiwist.twitch.feature_post.data.remote.request

data class LikeUpdateRequest(
    val parentId: String,
    val parentType: Int
)
