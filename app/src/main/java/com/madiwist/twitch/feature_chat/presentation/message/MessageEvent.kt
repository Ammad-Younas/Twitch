package com.madiwist.twitch.feature_chat.presentation.message

sealed class MessageEvent {
    data class EnteredMessage(val message: String) : MessageEvent()
    object SendMessage : MessageEvent()
    object RefreshMessages : MessageEvent()
}
