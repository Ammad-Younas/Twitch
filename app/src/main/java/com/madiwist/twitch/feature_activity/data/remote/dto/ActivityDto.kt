package com.madiwist.twitch.feature_activity.data.remote.dto

import com.madiwist.twitch.core.domain.models.Activity
import com.madiwist.twitch.core.util.DateFormatUtil
import com.madiwist.twitch.feature_activity.domain.util.ActivityType

data class ActivityDto (
    val timestamp: Long,
    val userId: String,
    val parentId: String,
    val type: Int,
    val username: String,
    val id: String
){
    fun toActivity() : Activity {
        return Activity(
            userId = userId,
            parentId = parentId,
            username = username,
            activityType = when(type){
                ActivityType.LikedPost.type -> ActivityType.LikedPost
                ActivityType.LikedComment.type -> ActivityType.LikedComment
                ActivityType.CommentedOnPost.type -> ActivityType.CommentedOnPost
                ActivityType.FollowedUser.type -> ActivityType.FollowedUser
                else -> ActivityType.FollowedUser
            },
            formatedTime = DateFormatUtil.timestampToFormatedString(
                timestamp = timestamp,
                pattern = "MMM dd, HH:mm"
            )
        )
    }
}