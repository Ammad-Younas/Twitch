package com.madiwist.twitch.feature_profile.data.remote

import com.madiwist.twitch.core.data.dto.response.BasicApiResponse
import com.madiwist.twitch.feature_profile.data.remote.response.ProfileResponse
import com.madiwist.twitch.feature_profile.data.remote.response.SkillDto
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface ProfileApi {
    @GET("/api/user/profile")
    suspend fun getProfile(
        @Query("userId") userId: String
    ) : BasicApiResponse<ProfileResponse>

    @GET("/api/skills/get")
    suspend fun getSkills() : BasicApiResponse<List<SkillDto>>

    @Multipart
    @PUT("/api/user/update")
    suspend fun updateProfile(
        @Part bannerImage : MultipartBody.Part?,
        @Part profileImage : MultipartBody.Part?,
        @Part updateProfileData: MultipartBody.Part
    ) : BasicApiResponse<Unit>


    companion object {
        const val BASE_URL = "http://10.0.2.2:8001/"
//        const val BASE_URL = "http://10.39.22.212:8001/"
    }
}