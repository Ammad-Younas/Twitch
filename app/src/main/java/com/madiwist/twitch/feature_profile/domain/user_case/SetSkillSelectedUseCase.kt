package com.madiwist.twitch.feature_profile.domain.user_case

import com.madiwist.twitch.feature_profile.domain.model.Skill
import com.madiwist.twitch.feature_profile.domain.util.ProfileConstant

class SetSkillSelectedUseCase {
    operator fun invoke(currentSelectedSkills: List<Skill>, skill: Skill): List<Skill> {
        return if (currentSelectedSkills.contains(skill)) {
            currentSelectedSkills - skill
        } else if (currentSelectedSkills.size < ProfileConstant.MAXIMUM_SKILLS) {
            currentSelectedSkills + skill
        } else {
            currentSelectedSkills
        }
    }
}