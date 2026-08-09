package com.madiwist.twitch.feature_activity.domain.use_case

import com.madiwist.twitch.core.domain.models.Activity
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.feature_activity.domain.repository.ActivityRepository

class GetActivityUseCase (
    private val repository: ActivityRepository
){
    suspend operator fun invoke(
        page: Int,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ) : Resource<List<Activity>> {
        return repository.getActivities(page, pageSize)
    }
}
