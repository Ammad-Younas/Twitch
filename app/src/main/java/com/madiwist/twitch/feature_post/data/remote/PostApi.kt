package com.madiwist.twitch.feature_post.data.remote

import com.madiwist.twitch.core.data.dto.response.BasicApiResponse
import com.madiwist.twitch.core.data.dto.response.UserItemDto
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.feature_post.data.remote.dto.CommentDto
import com.madiwist.twitch.feature_post.data.remote.dto.PostDto
import com.madiwist.twitch.feature_post.data.remote.request.CreateCommentRequest
import com.madiwist.twitch.feature_post.data.remote.request.LikeUpdateRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
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
    ): List<PostDto>

    @GET("/api/user/posts")
    suspend fun getPostsForProfile(
        @Query("userId") userId: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): List<PostDto>

    @Multipart
    @POST("/api/post/create")
    suspend fun createPost(
        @Part postData: MultipartBody.Part,
        @Part postImage: MultipartBody.Part
    ): BasicApiResponse<Unit>


    @GET("/api/post/details")
    suspend fun getPostDetails(
        @Query("postId") postId: String
    ): BasicApiResponse<PostDto>


    @POST("/api/comment/create")
    suspend fun createComment(
        @Body request: CreateCommentRequest
    ) : BasicApiResponse<Unit>

    @GET("/api/comment/get")
    suspend fun getCommentsForPost(
        @Query("postId") postId: String
    ): List<CommentDto>


    @POST("/api/like")
    suspend fun likeParent(
        @Body request: LikeUpdateRequest
    ) : BasicApiResponse<Unit>


    @DELETE("/api/unlike")
    suspend fun unlikeParent(
        @Query("parentId") parentId : String,
        @Query("parentType") parentType : Int
    ) : BasicApiResponse<Unit>


    @GET("/api/like/parent")
    suspend fun getLikesForParent(
        @Query("parentId") parentId: String
    ) : List<UserItemDto>

    companion object {
        const val BASE_URL = Constants.BASE_URL
    }
}