package com.madiwist.twitch.feature_chat.data.remote.data

import com.madiwist.twitch.core.util.DateFormatUtil
import com.madiwist.twitch.feature_chat.domain.model.Chat

data class ChatDto(
    val chatId: String,
    val remoteUserId: String?,
    val remoteUsername: String?,
    val remoteUserProfileUrl: String?,
    val lastMessage: String?,
    val timestamp: Long
) {
    fun toChat(): Chat? {
        return Chat(
            chatId = chatId,
            remoteUserId = remoteUserId ?: return null,
            remoteUsername = remoteUsername ?: return null,
            remoteUserProfileUrl = remoteUserProfileUrl ?: "",
            lastMessage = lastMessage ?: "",
            timestamp = DateFormatUtil.timestampToFormatedString(
                timestamp = timestamp,
                pattern = "MMM dd, HH:mm"
            )
        )
    }
}