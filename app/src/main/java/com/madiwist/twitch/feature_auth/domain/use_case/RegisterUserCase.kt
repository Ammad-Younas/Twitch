package com.madiwist.twitch.feature_auth.domain.use_case

import com.madiwist.twitch.core.domain.util.ValidationUtil
import com.madiwist.twitch.feature_auth.domain.models.RegisterResult
import com.madiwist.twitch.feature_auth.domain.repository.AuthRepository

class RegisterUserCase (private val repository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        username: String,
        password: String
    ): RegisterResult {

        val emailError = ValidationUtil.validateEmail(email)
        val usernameError = ValidationUtil.validateUsername(username)
        val passwordError = ValidationUtil.validatePassword(password)

        if (emailError != null || usernameError != null || passwordError != null){
            return RegisterResult(
                emailError = emailError,
                usernameError = usernameError,
                passwordError = passwordError,
            )
        }

        val result = repository.register(email = email.trim(), username = username.trim(), password = password.trim())

        return RegisterResult(
            result = result
        )
    }
}