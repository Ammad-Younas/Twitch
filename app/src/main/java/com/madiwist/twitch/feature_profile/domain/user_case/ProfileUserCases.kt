package com.madiwist.twitch.feature_profile.domain.user_case

data class ProfileUserCases(
    val getProfile: GetProfileUseCase,
    val getSkills: GetSkillUseCase,
    val updateProfile: UpdateProfileUseCase,
)
