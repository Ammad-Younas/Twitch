package com.madiwist.twitch.feature_chat.data.remote

import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.feature_chat.data.remote.data.ChatDto
import retrofit2.http.GET

interface ChatApi {
    @GET("/api/chat/chats")
    suspend fun getChatsForUser() : List<ChatDto>


    companion object {
        const val BASE_URL = Constants.BASE_URL
    }
}