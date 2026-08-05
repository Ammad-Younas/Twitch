package com.madiwist.twitch.feature_activity.presentation

import com.madiwist.twitch.core.domain.models.Activity

data class ActivityState(
    val activities: List<Activity> = emptyList(),
    val isLoading: Boolean = false,
)
