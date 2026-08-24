package com.madiwist.twitch.feature_chat.presentation.message

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.madiwist.twitch.core.domain.states.TwitchTextFieldState
import com.madiwist.twitch.core.presentation.util.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class MessageViewModel @Inject constructor() : ViewModel() {

    private val _messageTextFieldState = mutableStateOf(TwitchTextFieldState())
    val messageTextFieldState: State<TwitchTextFieldState> = _messageTextFieldState

    private val _messageState = mutableStateOf(MessageState())
    val messageState: State<MessageState> = _messageState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun onEvent(event: MessageEvent){
        when(event){
            is MessageEvent.EnteredMessage -> {
                _messageTextFieldState.value = messageTextFieldState.value.copy(
                    text = event.message
                )
            }
            is MessageEvent.SendMessage -> {

            }
        }
    }
}












