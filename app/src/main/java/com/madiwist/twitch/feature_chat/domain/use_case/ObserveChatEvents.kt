package com.madiwist.twitch.feature_chat.domain.use_case

import com.madiwist.twitch.feature_chat.data.remote.util.WebSocketEvent
import com.madiwist.twitch.feature_chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class ObserveChatEvents(
    private val repository: ChatRepository
) {
    operator fun invoke(): Flow<WebSocketEvent> {
        return repository.observeChatEvents()
    }
}
