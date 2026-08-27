package com.madiwist.twitch.feature_chat.domain.use_case

import com.madiwist.twitch.feature_chat.domain.repository.ChatRepository

class InitializeChat(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(url: String) {
        repository.initializeChat(url)
    }
}
