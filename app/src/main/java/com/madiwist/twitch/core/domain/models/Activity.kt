package com.madiwist.twitch.core.domain.models

import com.madiwist.twitch.feature_activity.domain.util.ActivityType

data class Activity(
    val userId: String,
    val parentId: String,
    val username: String,
    val activityType: ActivityType,
    val formatedTime: String,
)