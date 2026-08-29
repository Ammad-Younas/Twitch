package com.madiwist.twitch.feature_chat.presentation.chat

sealed class ChatEvent {
    object RefreshChats : ChatEvent()
}