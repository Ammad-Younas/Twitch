package com.madiwist.twitch.core.data.remote

import com.madiwist.twitch.core.data.dto.response.BasicApiResponse
import com.madiwist.twitch.core.domain.models.Post
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface PostApi {

    @GET("/api/post/get")
    suspend fun getPostsForFollows(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ) : List<Post>

    @GET("/api/user/posts")
    suspend fun getPostsForProfile(
        @Query("userId") userId: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ) : List<Post>

    @Multipart
    @POST("/api/post/create")
    suspend fun createPost(
        @Part postData : MultipartBody.Part,
        @Part postImage: MultipartBody.Part
    ) : BasicApiResponse<Unit>

    companion object {
        const val BASE_URL = "http://192.168.100.135:8001/"
    }
}