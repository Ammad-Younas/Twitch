package com.madiwist.twitch.feature_auth.domain.use_case

import com.madiwist.twitch.core.domain.util.ValidationUtil
import com.madiwist.twitch.feature_auth.domain.models.LoginResult
import com.madiwist.twitch.feature_auth.domain.repository.AuthRepository
import com.madiwist.twitch.feature_auth.presentation.util.AuthError

class LoginUseCase (
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ) : LoginResult {
        val emailError = ValidationUtil.validateEmail(email)
        val passwordError = if (password.isBlank()) AuthError.FieldEmpty else null

        if (emailError != null || passwordError != null){
            return LoginResult(
                emailError = emailError,
                passwordError = passwordError,
            )
        }

        val result = repository.login(email = email.trim(), password = password.trim())

        return LoginResult(
            result = result
        )
    }
}