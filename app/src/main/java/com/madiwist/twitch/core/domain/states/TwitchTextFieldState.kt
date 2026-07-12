package com.madiwist.twitch.core.domain.states

import com.madiwist.twitch.core.util.Error

data class TwitchTextFieldState (
    val text: String = "",
    val error: Error? = null
)