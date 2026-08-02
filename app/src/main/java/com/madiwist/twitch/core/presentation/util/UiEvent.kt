package com.madiwist.twitch.core.presentation.util

import com.madiwist.twitch.core.util.UiText

sealed class UiEvent {
    data class ShowSnackBar(val uiText: UiText): UiEvent()
    data class Navigate(val route: String): UiEvent()
    object NavigateUp: UiEvent()
    object Refresh: UiEvent()
}