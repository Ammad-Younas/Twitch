package com.madiwist.twitch.feature_post.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.madiwist.twitch.core.domain.models.Post

data class PostDto(
    @SerializedName("id", alternate = ["_id"])
    val id: Any?,
    val imageUrl: String?,
    val userId: String?,
    val username: String?,
    val timestamp: Long?,
    val description: String?,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
) {
    fun toPost(): Post {
        return Post(
            id = id?.toString() ?: "",
            imageUrl = imageUrl,
            userId = userId,
            username = username,
            timestamp = timestamp,
            description = description,
            likeCount = likeCount,
            commentCount = commentCount
        )
    }
}