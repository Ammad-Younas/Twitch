package com.madiwist.twitch.feature_chat.data.remote.ws.data

import com.madiwist.twitch.feature_chat.domain.model.Message
import java.text.DateFormat
import java.util.Date

data class WebSocketMessage(
    val fromId: String,
    val toId: String,
    val text: String,
    val timestamp: Long,
    val chatId: String?,
) {
    fun toMessage(): Message {
        return Message(
            fromId = fromId,
            toId = toId,
            text = text,
            timestamp = DateFormat
                .getDateInstance(DateFormat.DEFAULT)
                .format(Date(timestamp)),
            chatId = chatId,
        )
    }
}
