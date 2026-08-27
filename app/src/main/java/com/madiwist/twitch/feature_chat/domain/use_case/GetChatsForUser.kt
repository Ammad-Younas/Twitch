package com.madiwist.twitch.feature_chat.domain.use_case

import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.feature_chat.domain.model.Chat
import com.madiwist.twitch.feature_chat.domain.repository.ChatRepository

class GetChatsForUser(
    private val repository: ChatRepository
) {
    suspend operator fun invoke() : Resource<List<Chat>> {
        return repository.getChatsForUser()
    }
}