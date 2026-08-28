package com.madiwist.twitch.feature_chat.data.remote.data

import com.madiwist.twitch.core.util.DateFormatUtil
import com.madiwist.twitch.feature_chat.domain.model.Message

data class MessageDto(
    val fromId: String,
    val toId: String,
    val text: String,
    val timestamp: Long,
    val chatId: String?,
    val id: String
) {
    fun toMessage() : Message {
        return Message(
            fromId = fromId,
            toId = toId,
            text = text,
            timestamp = DateFormatUtil.timestampToFormatedString(
                timestamp = timestamp,
                pattern = "HH:mm"
            ),
            chatId = chatId,
        )
    }
}