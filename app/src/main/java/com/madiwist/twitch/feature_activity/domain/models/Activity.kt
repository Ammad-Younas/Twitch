package com.madiwist.twitch.feature_activity.domain.models

import com.madiwist.twitch.feature_activity.domain.util.ActivityAction

data class Activity(
    val username: String,
    val actionType: ActivityAction,
    val formatedTime: String,
)
