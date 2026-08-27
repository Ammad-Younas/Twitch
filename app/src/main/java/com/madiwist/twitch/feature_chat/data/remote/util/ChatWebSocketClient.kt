package com.madiwist.twitch.feature_chat.data.remote.util

import com.madiwist.twitch.feature_chat.data.remote.data.WebSocketClientMessage
import com.madiwist.twitch.feature_chat.data.remote.data.WebSocketServerMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.close
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatWebSocketClient @Inject constructor(
    private val client: HttpClient
) {
    private var session: DefaultClientWebSocketSession? = null

    private val _messages = MutableSharedFlow<WebSocketServerMessage>()
    val messages: Flow<WebSocketServerMessage> = _messages.asSharedFlow()

    private val _events = MutableSharedFlow<WebSocketEvent>()
    val events: Flow<WebSocketEvent> = _events.asSharedFlow()

    suspend fun connect(url: String) {
        try {
            _events.emit(WebSocketEvent.OnGoing)
            client.webSocket(url) {
                session = this
                _events.emit(WebSocketEvent.Connected)
                try {
                    while (true) {
                        val message = receiveDeserialized<WebSocketServerMessage>()
                        _messages.emit(message)
                    }
                } catch (e: Exception) {
                    _events.emit(WebSocketEvent.Error(e.localizedMessage))
                } finally {
                    session = null
                    _events.emit(WebSocketEvent.Disconnected)
                }
            }
        } catch (e: Exception) {
            _events.emit(WebSocketEvent.Error(e.localizedMessage))
        }
    }

    suspend fun send(message: WebSocketClientMessage) {
        try {
            session?.sendSerialized(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun disconnect() {
        session?.close()
        session = null
    }
}