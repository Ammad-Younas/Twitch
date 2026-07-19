package com.madiwist.twitch.feature_auth.domain.models

import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.feature_auth.presentation.util.AuthError

data class LoginResult(
    val emailError: AuthError? = null,
    val passwordError: AuthError? = null,
    val result: SimpleResource? = null
)