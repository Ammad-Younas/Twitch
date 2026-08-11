package com.madiwist.twitch.feature_profile.presentation.profile

import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.feature_profile.domain.model.Profile

data class ProfileState(
    val profile: Profile? = null,
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val endReached: Boolean = false,
    val isLogoutDialogueVisible: Boolean = false,
    val page: Int = 0
)
