package com.madiwist.twitch.feature_auth.data.remote

import com.madiwist.twitch.feature_auth.data.remote.request.CreateAccountRequest
import com.madiwist.twitch.core.data.dto.response.BasicApiResponse
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.feature_auth.data.remote.request.LoginRequest
import com.madiwist.twitch.feature_auth.data.remote.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/user/create")
    suspend fun register(@Body request: CreateAccountRequest) : BasicApiResponse<Unit>

    @GET("/api/user/authenticate")
    suspend fun authenticate()

    @POST("/api/user/login")
    suspend fun login(@Body request: LoginRequest) : BasicApiResponse<AuthResponse>

    companion object {
        const val BASE_URL = Constants.BASE_URL
    }
}