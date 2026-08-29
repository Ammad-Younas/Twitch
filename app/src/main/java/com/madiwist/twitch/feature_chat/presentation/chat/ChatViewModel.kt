package com.madiwist.twitch.feature_chat.presentation.chat

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_chat.domain.use_case.ChatUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _chatState = mutableStateOf(ChatState())
    val chatState: State<ChatState> = _chatState

    init {
        loadChats()
    }

    fun onEvent(event: ChatEvent) {
        when(event) {
            is ChatEvent.RefreshChats -> {
                loadChats()
            }
        }
    }

    private fun loadChats() {
        viewModelScope.launch {
            _chatState.value = chatState.value.copy(
                isLoading = true
            )
            when (val result = chatUseCases.getChatsForUser()) {
                is Resource.Success -> {
                    _chatState.value = chatState.value.copy(
                        chats = result.data ?: emptyList(),
                        isLoading = false
                    )
                }

                is Resource.Error -> {
                    _chatState.value = chatState.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowSnackBar(result.uiText ?: UiText.unknownError()))

                }
            }
        }
    }
}