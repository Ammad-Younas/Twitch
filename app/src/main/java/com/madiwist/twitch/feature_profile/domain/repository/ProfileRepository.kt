package com.madiwist.twitch.feature_profile.domain.repository

import android.net.Uri
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.feature_profile.domain.model.Profile
import com.madiwist.twitch.feature_profile.domain.model.Skill
import com.madiwist.twitch.feature_profile.domain.model.UpdateProfileData

interface ProfileRepository {
    suspend fun getProfile(userId: String): Resource<Profile>
    suspend fun getSkills(): Resource<List<Skill>>
    suspend fun updateProfile(
        bannerImageUri: Uri?,
        profileImageUri: Uri?,
        userProfileData: UpdateProfileData
    ) : SimpleResource
}