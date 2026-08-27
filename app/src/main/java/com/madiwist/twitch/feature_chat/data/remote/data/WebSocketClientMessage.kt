package com.madiwist.twitch.feature_chat.data.remote.data

import kotlinx.serialization.Serializable

@Serializable
data class WebSocketClientMessage(
    val toId: String,
    val text: String,
    val chatId: String?,
)

