package com.madiwist.twitch.feature_chat.data.remote.util

import com.madiwist.twitch.feature_chat.data.remote.data.WebSocketServerMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatWebSocketClient @Inject constructor(
    private val client: HttpClient,
    private val json: Json
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
                        val frame = incoming.receive()
                        if (frame is Frame.Text) {
                            val frameText = frame.readText()
                            val delimiterIndex = frameText.indexOf('#')
                            if (delimiterIndex != -1) {
                                val type = frameText.substring(0, delimiterIndex).toIntOrNull()
                                if (type == WebSocketObject.MESSAGE.ordinal) {
                                    val jsonString = frameText.substring(delimiterIndex + 1)
                                    val message = json.decodeFromString<WebSocketServerMessage>(jsonString)
                                    _messages.emit(message)
                                }
                            }
                        }
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

    suspend fun send(message: WebSocketServerMessage) {
        try {
            val payload = "${WebSocketObject.MESSAGE.ordinal}#${json.encodeToString(message)}"
            session?.send(Frame.Text(payload))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun disconnect() {
        session?.close()
        session = null
    }
}
