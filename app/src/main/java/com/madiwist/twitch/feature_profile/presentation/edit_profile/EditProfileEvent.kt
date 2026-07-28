package com.madiwist.twitch.feature_profile.presentation.edit_profile

import android.graphics.Bitmap
import com.madiwist.twitch.feature_profile.domain.model.Skill

sealed class EditProfileEvent {
    data class EnteredUsername(val value: String) : EditProfileEvent()
    data class EnteredGithubUrl(val value: String) : EditProfileEvent()
    data class EnteredInstagramUrl(val value: String) : EditProfileEvent()
    data class EnteredLinkedinUrl(val value: String) : EditProfileEvent()
    data class EnteredBio(val value: String) : EditProfileEvent()

    data class CropBannerImage(val bitmap: Bitmap?) : EditProfileEvent()

    data class CropProfileImage(val bitmap: Bitmap?) : EditProfileEvent()

    data class SetSkillsSelected(val skill: Skill) : EditProfileEvent()

    object UpdateProfile : EditProfileEvent()
}