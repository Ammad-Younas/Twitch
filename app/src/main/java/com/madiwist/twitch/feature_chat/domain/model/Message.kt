package com.madiwist.twitch.feature_chat.domain.model

data class Message(
    val fromId: String,
    val toId: String,
    val text: String,
    val timestamp: String,
    val chatId: String,
    val id: String,
)
