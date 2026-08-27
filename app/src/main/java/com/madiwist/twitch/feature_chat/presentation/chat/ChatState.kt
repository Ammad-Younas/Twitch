package com.madiwist.twitch.feature_chat.presentation.chat

import com.madiwist.twitch.feature_chat.domain.model.Chat

data class ChatState(
    val chats: List<Chat> = emptyList(),
    val isLoading : Boolean = false
)
