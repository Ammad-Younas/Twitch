package com.madiwist.twitch.feature_profile.data.repository

import android.content.SharedPreferences
import android.net.Uri
import androidx.core.content.edit
import androidx.core.net.toFile
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.madiwist.twitch.R
import com.madiwist.twitch.core.data.remote.PostApi
import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.core.domain.models.UserItem
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_post.data.paging.PostSource
import com.madiwist.twitch.feature_profile.data.remote.ProfileApi
import com.madiwist.twitch.feature_profile.data.remote.request.FollowUpdateRequest
import com.madiwist.twitch.feature_profile.domain.model.Profile
import com.madiwist.twitch.feature_profile.domain.model.Skill
import com.madiwist.twitch.feature_profile.domain.model.UpdateProfileData
import com.madiwist.twitch.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import retrofit2.HttpException

class ProfileRepositoryImpl(
    private val profileApi: ProfileApi,
    private val postApi: PostApi,
    private val gson: Gson,
    private val sharedPreferences: SharedPreferences
) : ProfileRepository {

    override suspend fun getProfile(userId: String): Resource<Profile> {
        return try {
            val response = profileApi.getProfile(userId)
            if (response.success) {
                val profile = response.data?.toProfile()
                if (profile?.isOwnProfile == true) {
                    sharedPreferences.edit {
                        putString(Constants.KEY_USER_ID, profile.userId)
                    }
                }
                Resource.Success(profile)
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
            val response = profileApi.updateProfile(

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
            val response = profileApi.getSkills()
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

    override fun getPostsPaged(userId: String): Flow<PagingData<Post>> {
        return Pager(PagingConfig(pageSize = Constants.PAGE_SIZE_POSTS)) {
            PostSource(postApi, PostSource.Source.Profile(userId))
        }.flow
    }

    override suspend fun searchUser(query: String): Resource<List<UserItem>> {
        return try {
            val response = profileApi.searchUser(query)
                Resource.Success(
                    data = response.map { it.toUserItem() }
                )
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

    override suspend fun followUser(userId: String): SimpleResource {
        return try {
            val response = profileApi.followUser(
                request = FollowUpdateRequest(userId)
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

    override suspend fun unfollowUser(userId: String): SimpleResource {
        return try {
            val response = profileApi.unfollowUser(userId = userId)
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
}