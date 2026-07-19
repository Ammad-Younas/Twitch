package com.madiwist.twitch.feature_auth.domain.use_case

import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.feature_auth.domain.repository.AuthRepository

class AuthenticateUseCase (
    private val repository: AuthRepository
) {
    suspend operator fun invoke() : SimpleResource {
        return repository.authenticate()
    }
}