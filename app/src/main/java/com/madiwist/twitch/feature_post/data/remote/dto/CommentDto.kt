package com.madiwist.twitch.feature_post.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.madiwist.twitch.core.domain.models.Comment
import com.madiwist.twitch.core.util.DateFormatUtil

data class CommentDto(
    @SerializedName("id", alternate = ["_id"])
    val commentId: Any?,
    val username: String,
    val profilePictureUrl: String,
    val timeStamp: Long,
    val comment: String,
    val isLiked: Boolean,
    val likeCount: Int
) {
    fun toComment() : Comment {
        return Comment(
            commentId = commentId?.toString() ?: "",
            username = username,
            profilePictureUrl = profilePictureUrl,
            timeStamp = DateFormatUtil.timestampToFormatedString(
                timestamp = timeStamp,
                pattern = "MMM dd, HH:mm"
            ),
            comment = comment,
            isLiked = isLiked,
            likeCount = likeCount
        )
    }
}