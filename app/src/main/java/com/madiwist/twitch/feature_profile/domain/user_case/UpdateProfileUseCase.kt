package com.madiwist.twitch.feature_profile.domain.user_case

import android.net.Uri
import com.madiwist.twitch.R
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_profile.domain.model.UpdateProfileData
import com.madiwist.twitch.feature_profile.domain.repository.ProfileRepository

class UpdateProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        bannerUri: Uri?,
        profilePictureUri: Uri?,
        updateProfileData: UpdateProfileData
    ) : SimpleResource {
        if (updateProfileData.username.isBlank()) {
            return Resource.Error(UiText.StringResource(R.string.error_username_empty))
        }
        return repository.updateProfile(
            bannerImageUri = bannerUri,
            profileImageUri = profilePictureUri,
            userProfileData = updateProfileData
        )
    }
}