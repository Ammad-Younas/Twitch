package com.madiwist.twitch.feature_chat.presentation.message

import com.madiwist.twitch.feature_chat.domain.model.Message

data class MessageState (
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val page: Int = 0,
    val endReached: Boolean = false,
    val ownUserId: String = ""
)
