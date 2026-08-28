package com.madiwist.twitch.feature_chat.domain.use_case

data class ChatUseCases(
    val sendMessage: SendMessage,
    val observeMessages: ObserveMessages,
    val observeChatEvents: ObserveChatEvents,
    val initializeChat: InitializeChat,
    val disconnectChat: DisconnectChat,
    val getChatsForUser: GetChatsForUser,
    val getMessagesForChat: GetMessagesForChat
)
