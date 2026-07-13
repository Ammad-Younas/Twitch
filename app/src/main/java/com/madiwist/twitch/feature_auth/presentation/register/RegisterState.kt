package com.madiwist.twitch.feature_auth.presentation.register

import com.madiwist.twitch.core.util.UiText

data class RegisterState(
    val successful: Boolean? = null,
    val message: UiText? = null,
    val isLoading: Boolean = false
)
