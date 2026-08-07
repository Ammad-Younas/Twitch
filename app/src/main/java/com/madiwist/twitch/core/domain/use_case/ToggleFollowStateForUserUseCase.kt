package com.madiwist.twitch.core.domain.use_case

import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.feature_profile.domain.repository.ProfileRepository

class ToggleFollowStateForUserUseCase (
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: String, isFollowing: Boolean) : SimpleResource {
        return if (isFollowing) {
            repository.unfollowUser(userId)
        } else {
            repository.followUser(userId)
        }
    }
}