package com.madiwist.twitch.feature_activity.domain.repository

import com.madiwist.twitch.core.domain.models.Activity
import com.madiwist.twitch.core.util.Resource

interface ActivityRepository {
    suspend fun getActivities(page: Int, pageSize: Int): Resource<List<Activity>>
}
