package com.madiwist.twitch.feature_profile.domain.user_case

import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.feature_profile.domain.model.Profile
import com.madiwist.twitch.feature_profile.domain.repository.ProfileRepository

class GetProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: String): Resource<Profile> {
        return repository.getProfile(userId)
    }
}