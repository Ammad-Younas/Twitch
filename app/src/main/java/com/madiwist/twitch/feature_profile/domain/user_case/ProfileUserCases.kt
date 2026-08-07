package com.madiwist.twitch.feature_profile.domain.user_case

import com.madiwist.twitch.core.domain.use_case.ToggleFollowStateForUserUseCase

data class ProfileUserCases(
    val getProfile: GetProfileUseCase,
    val getSkills: GetSkillUseCase,
    val updateProfile: UpdateProfileUseCase,
    val setSkills: SetSkillSelectedUseCase,
    val getPosts: GetPostsForProfileUserCase,
    val searchUser: SearchUserUseCase,
    val toggleFollowStateForUser: ToggleFollowStateForUserUseCase
)
