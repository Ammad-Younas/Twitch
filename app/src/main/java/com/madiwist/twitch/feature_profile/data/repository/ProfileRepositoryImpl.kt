package com.madiwist.twitch.feature_profile.data.repository

import android.net.Uri
import androidx.core.net.toFile
import com.google.gson.Gson
import com.madiwist.twitch.R
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_profile.data.remote.ProfileApi
import com.madiwist.twitch.feature_profile.domain.model.Profile
import com.madiwist.twitch.feature_profile.domain.model.Skill
import com.madiwist.twitch.feature_profile.domain.model.UpdateProfileData
import com.madiwist.twitch.feature_profile.domain.repository.ProfileRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import retrofit2.HttpException

class ProfileRepositoryImpl(
    private val api: ProfileApi,
    private val gson: Gson
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

    override suspend fun updateProfile(
        bannerImageUri: Uri?,
        profileImageUri: Uri?,
        userProfileData: UpdateProfileData
    ): SimpleResource {
        val bannerFile = bannerImageUri?.toFile()
        val profilePictureFile = profileImageUri?.toFile()
        return try {
            val response = api.updateProfile(

                bannerImage = bannerFile?.let { banner->
                    MultipartBody.Part.createFormData(
                        name = "banner_image",
                        filename = banner.name,
                        body = banner.asRequestBody(),
                    )
                },
                profileImage = profilePictureFile?.let { profile->
                    MultipartBody.Part.createFormData(
                        name = "profile_picture",
                        filename = profile.name,
                        body = profile.asRequestBody(),
                    )
                },
                updateProfileData = MultipartBody.Part.createFormData("update_profile_data", gson.toJson(userProfileData))

            )
            if (response.success) {
                Resource.Success(Unit)
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

    override suspend fun getSkills(): Resource<List<Skill>> {
        return try {
            val response = api.getSkills()
            if (response.success) {
                Resource.Success(
                    data = response.data?.map { it.toSkill() }
                )
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