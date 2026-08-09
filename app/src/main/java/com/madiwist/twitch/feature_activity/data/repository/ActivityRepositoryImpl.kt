package com.madiwist.twitch.feature_activity.data.repository

import com.madiwist.twitch.R
import com.madiwist.twitch.core.domain.models.Activity
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_activity.data.remote.ActivityApi
import com.madiwist.twitch.feature_activity.domain.repository.ActivityRepository
import retrofit2.HttpException
import java.io.IOException

class ActivityRepositoryImpl (
    private val api: ActivityApi
) : ActivityRepository {

    override suspend fun getActivities(page: Int, pageSize: Int): Resource<List<Activity>> {
        return try {
            val response = api.getActivities(page, pageSize)
            Resource.Success(response.map { it.toActivity() })
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server),
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_something_went_wrong)
            )
        }
    }
}
