package com.madiwist.twitch.domain.models

import com.madiwist.twitch.domain.util.ActivityAction

data class Activity(
    val username: String,
    val actionType: ActivityAction,
    val formatedTime: String,
)
