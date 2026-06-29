package com.madiwist.twitch.presentation.create_post

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.madiwist.twitch.presentation.utils.states.TwitchTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor() : ViewModel() {
    private val _descriptionState = mutableStateOf(TwitchTextFieldState())
    val descriptionState: State<TwitchTextFieldState> = _descriptionState

    fun setDescriptionState(state: TwitchTextFieldState) {
        _descriptionState.value = state
    }
}