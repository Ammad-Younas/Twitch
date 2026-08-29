package com.madiwist.twitch.feature_chat.data.remote.data

import com.google.gson.annotations.SerializedName
import com.madiwist.twitch.core.util.DateFormatUtil
import com.madiwist.twitch.feature_chat.domain.model.Chat

data class ChatDto(
    @SerializedName("chatId", alternate = ["chat_id"])
    val chatId: String,
    @SerializedName("remoteUserId", alternate = ["remote_user_id"])
    val remoteUserId: String?,
    @SerializedName("remoteUsername", alternate = ["remote_username"])
    val remoteUsername: String?,
    @SerializedName("remoteUserProfileUrl", alternate = ["remote_user_profile_url"])
    val remoteUserProfileUrl: String?,
    @SerializedName("lastMessage", alternate = ["last_message"])
    val lastMessage: String?,
    @SerializedName("timestamp", alternate = ["time_stamp"])
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