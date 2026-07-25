package com.madiwist.twitch.feature_profile.presentation.profile

import com.madiwist.twitch.feature_profile.domain.model.Profile

data class ProfileState(
    val profile: Profile? = null,
    val isLoading: Boolean = false
)
