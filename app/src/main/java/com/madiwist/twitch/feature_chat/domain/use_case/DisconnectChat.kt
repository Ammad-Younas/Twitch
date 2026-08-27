package com.madiwist.twitch.feature_chat.domain.use_case

import com.madiwist.twitch.feature_chat.domain.repository.ChatRepository

class DisconnectChat(
    private val repository: ChatRepository
) {
    suspend operator fun invoke() {
        repository.disconnectChat()
    }
}
