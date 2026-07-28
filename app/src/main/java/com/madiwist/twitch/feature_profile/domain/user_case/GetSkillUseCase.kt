package com.madiwist.twitch.feature_profile.domain.user_case

import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.feature_profile.domain.model.Skill
import com.madiwist.twitch.feature_profile.domain.repository.ProfileRepository

class GetSkillUseCase (
    private val repository: ProfileRepository
) {
    suspend operator fun invoke() : Resource<List<Skill>> {
        return repository.getSkills()
    }
}