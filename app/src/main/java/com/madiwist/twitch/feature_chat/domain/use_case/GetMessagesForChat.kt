package com.madiwist.twitch.feature_chat.domain.use_case

import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.feature_chat.domain.model.Message
import com.madiwist.twitch.feature_chat.domain.repository.ChatRepository

class GetMessagesForChat(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(
        chatId: String,
        page: Int,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ) : Resource<List<Message>> {
        return repository.getMessagesForChat(
            chatId = chatId,
            page = page,
            pageSize = pageSize
        )
    }
}