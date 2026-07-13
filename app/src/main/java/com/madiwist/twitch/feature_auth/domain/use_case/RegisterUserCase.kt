package com.madiwist.twitch.feature_auth.domain.use_case

import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.feature_auth.domain.repository.AuthRepository

class RegisterUserCase (private val repository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        username: String,
        password: String
    ): SimpleResource {
        return repository.register(
            email = email.trim(),
            username = username.trim(),
            password = password.trim()
        )
    }
}