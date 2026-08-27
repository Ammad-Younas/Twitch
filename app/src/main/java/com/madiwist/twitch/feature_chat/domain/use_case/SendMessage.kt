package com.madiwist.twitch.feature_chat.domain.use_case

import com.madiwist.twitch.feature_chat.domain.repository.ChatRepository

class SendMessage(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(toId: String, text: String, chatId: String?) {
        if (text.isBlank()){
            return
        }
        repository.sendMessage(toId, text.trim(), chatId)
    }
}
