package com.madiwist.twitch.feature_profile.domain.repository

import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.feature_profile.domain.model.Profile

interface ProfileRepository {
    suspend fun getProfile(userId: String): Resource<Profile>
}