package com.madiwist.twitch.feature_chat.domain.model

data class Chat(
    val chatId: String,
    val remoteUserId: String,
    val remoteUsername: String,
    val remoteUserProfileUrl: String,
    val lastMessage: String,
    val timestamp: Long?
)
