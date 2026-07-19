package com.madiwist.twitch.feature_auth.domain.models

import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.feature_auth.presentation.util.AuthError

data class RegisterResult(
    val emailError: AuthError? = null,
    val usernameError: AuthError? = null,
    val passwordError: AuthError? = null,
    val result: SimpleResource? = null
)
