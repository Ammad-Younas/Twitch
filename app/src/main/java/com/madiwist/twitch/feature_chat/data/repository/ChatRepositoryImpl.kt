package com.madiwist.twitch.feature_chat.data.repository

import com.madiwist.twitch.R
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_chat.data.remote.ChatApi
import com.madiwist.twitch.feature_chat.data.remote.data.WebSocketServerMessage
import com.madiwist.twitch.feature_chat.data.remote.util.ChatWebSocketClient
import com.madiwist.twitch.feature_chat.data.remote.util.WebSocketEvent
import com.madiwist.twitch.feature_chat.domain.model.Chat
import com.madiwist.twitch.feature_chat.domain.model.Message
import com.madiwist.twitch.feature_chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.IOException
import retrofit2.HttpException

class ChatRepositoryImpl(
    private val chatApi: ChatApi,
    private val client: ChatWebSocketClient
) : ChatRepository {
    override suspend fun getChatsForUser(): Resource<List<Chat>> {
        return try {
            val chats = chatApi.getChatsForUser().mapNotNull { it.toChat() }
            Resource.Success(data = chats)
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server),
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_something_went_wrong)
            )
        }
    }

    override suspend fun getMessagesForChat(
        chatId: String,
        page: Int,
        pageSize: Int
    ): Resource<List<Message>> {
        return try {
            val messages = chatApi.getMessagesForChat(chatId = chatId, page = page, pageSize = pageSize).map { it.toMessage() }
            Resource.Success(data = messages)
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server),
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_something_went_wrong)
            )
        }
    }

    override fun observeChatEvents(): Flow<WebSocketEvent> {
        return client.events
    }

    override fun observeMessages(): Flow<Message> {
        return client.messages.map { it.toMessage() }
    }

    override suspend fun sendMessage(fromId: String, toId: String, text: String, chatId: String?) {
        client.send(
            WebSocketServerMessage(
                fromId = fromId,
                toId = toId,
                text = text,
                chatId = chatId,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override suspend fun initializeChat(url: String) {
        client.connect(url)
    }

    override suspend fun disconnectChat() {
        client.disconnect()
    }
}
