package com.madiwist.twitch.feature_activity.domain.use_case

import androidx.paging.PagingData
import com.madiwist.twitch.core.domain.models.Activity
import com.madiwist.twitch.feature_activity.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow

class GetActivityUseCase (
    private val repository: ActivityRepository
){
    operator fun invoke() : Flow<PagingData<Activity>> {
        return repository.activities
    }
}
