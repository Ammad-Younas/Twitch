package com.madiwist.twitch.feature_profile.domain.user_case

import com.madiwist.twitch.feature_profile.domain.repository.ProfileRepository

class LogoutUseCase (
    private val repository: ProfileRepository
) {
    operator fun invoke() {
        repository.logout()
    }
}