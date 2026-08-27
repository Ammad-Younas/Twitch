package com.madiwist.twitch.feature_chat.domain.repository

import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.feature_chat.data.remote.util.WebSocketEvent
import com.madiwist.twitch.feature_chat.domain.model.Chat
import com.madiwist.twitch.feature_chat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun getChatsForUser(): Resource<List<Chat>>

    fun observeChatEvents(): Flow<WebSocketEvent>

    fun observeMessages(): Flow<Message>

    suspend fun sendMessage(toId: String, text: String, chatId: String?)

    suspend fun initializeChat(url: String)

    suspend fun disconnectChat()
}
