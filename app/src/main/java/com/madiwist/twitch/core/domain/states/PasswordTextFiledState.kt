package com.madiwist.twitch.core.domain.states

import com.madiwist.twitch.core.util.Error

data class PasswordTextFiledState(
    val text: String = "",
    val error: Error? = null,
    val isPasswordVisible: Boolean = false
)
