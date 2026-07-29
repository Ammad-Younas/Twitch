package com.madiwist.twitch.feature_profile.domain.user_case

import android.net.Uri
import com.madiwist.twitch.R
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_profile.domain.model.UpdateProfileData
import com.madiwist.twitch.feature_profile.domain.repository.ProfileRepository
import com.madiwist.twitch.feature_profile.util.AppPatterns

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

        if (updateProfileData.gitHubUrl.isNullOrBlank() || !AppPatterns.GITHUB.matches(updateProfileData.gitHubUrl)) {
            return Resource.Error(
                UiText.StringResource(R.string.error_invalid_github_url)
            )
        }

        if (updateProfileData.instagramUrl.isNullOrBlank() || !AppPatterns.INSTAGRAM.matches(updateProfileData.instagramUrl)) {
            return Resource.Error(
                UiText.StringResource(R.string.error_invalid_instagram_url)
            )
        }

        if (updateProfileData.linkedInUrl.isNullOrBlank() || !AppPatterns.LINKEDIN.matches(updateProfileData.linkedInUrl)) {
            return Resource.Error(
                UiText.StringResource(R.string.error_invalid_linkedin_url)
            )
        }

        return repository.updateProfile(
            bannerImageUri = bannerUri,
            profileImageUri = profilePictureUri,
            userProfileData = updateProfileData
        )
    }
}