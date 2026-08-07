package com.madiwist.twitch.feature_profile.presentation.profile

import com.madiwist.twitch.core.domain.models.Post

sealed class ProfileEvent {
    data class GetProfile(val userId: String) : ProfileEvent()
    data class LikePost(val post: Post) : ProfileEvent()
}
