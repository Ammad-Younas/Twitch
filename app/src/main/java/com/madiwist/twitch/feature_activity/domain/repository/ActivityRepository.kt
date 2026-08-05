package com.madiwist.twitch.feature_activity.domain.repository

import androidx.paging.PagingData
import com.madiwist.twitch.core.domain.models.Activity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    val activities: Flow<PagingData<Activity>>
}