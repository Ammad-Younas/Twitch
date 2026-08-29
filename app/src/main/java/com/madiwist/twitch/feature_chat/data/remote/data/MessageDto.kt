package com.madiwist.twitch.feature_chat.data.remote.data

import com.google.gson.annotations.SerializedName
import com.madiwist.twitch.core.util.DateFormatUtil
import com.madiwist.twitch.feature_chat.domain.model.Message

data class MessageDto(
    @SerializedName("fromId", alternate = ["from_id"])
    val fromId: String,
    @SerializedName("toId", alternate = ["to_id"])
    val toId: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("timestamp", alternate = ["time_stamp"])
    val timestamp: Long,
    @SerializedName("chatId", alternate = ["chat_id"])
    val chatId: String?,
    @SerializedName("id", alternate = ["_id"])
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