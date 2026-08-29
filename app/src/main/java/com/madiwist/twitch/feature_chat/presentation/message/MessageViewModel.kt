package com.madiwist.twitch.feature_chat.presentation.message

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madiwist.twitch.R
import com.madiwist.twitch.core.domain.states.TwitchTextFieldState
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.core.util.paging.DefaultPaginator
import com.madiwist.twitch.feature_chat.data.remote.util.WebSocketEvent
import com.madiwist.twitch.feature_chat.domain.use_case.ChatUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases,
    sharedPreferences: SharedPreferences,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _messageTextFieldState = mutableStateOf(TwitchTextFieldState())
    val messageTextFieldState: State<TwitchTextFieldState> = _messageTextFieldState

    private val _messageState = mutableStateOf(MessageState())
    val messageState: State<MessageState> = _messageState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val paginator = DefaultPaginator(
        initialKey = 0,
        onLoadUpdated = { isLoading ->
            _messageState.value = messageState.value.copy(
                isLoading = isLoading
            )
        },
        onRequest = { nextPage ->
            val chatId = savedStateHandle.get<String>("chatId") ?: ""
            if (chatId != "null" && chatId.isNotBlank()) {
                chatUseCases.getMessagesForChat(chatId, nextPage)
            } else {
                Resource.Success(data = emptyList())
            }
        },
        getNextKey = {
            messageState.value.page + 1
        },
        onError = { error ->
            _eventFlow.emit(UiEvent.ShowSnackBar(error ?: UiText.unknownError()))
        },
        onSuccess = { items, newKey ->
            _messageState.value = messageState.value.copy(
                messages = messageState.value.messages + items,
                page = newKey,
                endReached = items.size < Constants.DEFAULT_PAGE_SIZE
            )
        }
    )

    init {
        val ownUserId = sharedPreferences.getString(Constants.KEY_USER_ID, "") ?: ""
        _messageState.value = messageState.value.copy(
            ownUserId = ownUserId
        )
        viewModelScope.launch {
            chatUseCases.initializeChat(Constants.WS_BASE_URL + "?userId=$ownUserId")
        }
        observeChatEvents()
        observeMessages()
        loadNextMessages()
    }

    private var isConnected = false

    private fun observeChatEvents() {
        chatUseCases.observeChatEvents()
            .onEach { event ->
                when (event) {
                    is WebSocketEvent.Connected -> {
                        isConnected = true
                        Timber.d("Connected to WebSocket")
                    }
                    is WebSocketEvent.Disconnected -> {
                        isConnected = false
                        Timber.d("Disconnected from WebSocket")
                    }
                    is WebSocketEvent.Error -> {
                        isConnected = false
                        Timber.e("WebSocket Error: ${event.message}")
                    }
                    is WebSocketEvent.OnGoing -> {
                        isConnected = false
                        Timber.d("Connecting to WebSocket...")
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun observeMessages() {
        chatUseCases.observeMessages()
            .onEach { message ->
                if (savedStateHandle.get<String>("chatId") == "null" && message.chatId != null) {
                    savedStateHandle["chatId"] = message.chatId
                }
                _messageState.value = messageState.value.copy(
                    messages = listOf(message) + messageState.value.messages
                )
            }.launchIn(viewModelScope)
    }


    fun onEvent(event: MessageEvent) {
        when (event) {
            is MessageEvent.EnteredMessage -> {
                _messageTextFieldState.value = _messageTextFieldState.value.copy(
                    text = event.message
                )
            }

            is MessageEvent.SendMessage -> {
                sendMessage()
            }

            is MessageEvent.RefreshMessages -> {
                paginator.reset()
                _messageState.value = MessageState(
                    ownUserId = messageState.value.ownUserId
                )
                loadNextMessages()
            }
        }
    }

    private fun sendMessage() {
        val text = messageTextFieldState.value.text
        val remoteUserId = savedStateHandle.get<String>("remoteUserId") ?: ""
        var chatId = savedStateHandle.get<String>("chatId")
        if (chatId == "null") {
            chatId = null
        }
        if (text.isBlank()) return

        if (!isConnected) {
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowSnackBar(UiText.StringResource(R.string.error_not_connected)))
            }
            return
        }

        viewModelScope.launch {
            chatUseCases.sendMessage(
                fromId = messageState.value.ownUserId,
                toId = remoteUserId,
                text = text,
                chatId = chatId
            )
            _messageTextFieldState.value = TwitchTextFieldState()
        }
    }

    fun loadNextMessages() {
        if (messageState.value.endReached || messageState.value.isLoading) return
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }
}












