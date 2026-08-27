package com.madiwist.twitch.feature_chat.data.remote.util

sealed class WebSocketEvent {
    object Connected : WebSocketEvent()
    object OnGoing : WebSocketEvent()
    object Disconnected : WebSocketEvent()
    data class Error(val message: String?) : WebSocketEvent()
}
