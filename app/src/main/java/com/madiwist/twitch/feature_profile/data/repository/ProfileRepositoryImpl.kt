package com.madiwist.twitch.feature_profile.data.repository

import com.madiwist.twitch.R
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_profile.data.remote.ProfileApi
import com.madiwist.twitch.feature_profile.domain.model.Profile
import com.madiwist.twitch.feature_profile.domain.repository.ProfileRepository
import okio.IOException
import retrofit2.HttpException

class ProfileRepositoryImpl(
    private val api: ProfileApi
) : ProfileRepository {
    override suspend fun getProfile(userId: String): Resource<Profile> {
        return try {
            val response = api.getProfile(userId)
            if (response.success) {
                Resource.Success(response.data?.toProfile())
            } else {
                response.message?.let { msg ->
                    Resource.Error(UiText.DynamicString(msg))
                } ?: Resource.Error(UiText.StringResource(R.string.unknown_error))
            }
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