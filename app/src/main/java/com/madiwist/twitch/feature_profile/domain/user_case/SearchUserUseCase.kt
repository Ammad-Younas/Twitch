package com.madiwist.twitch.feature_profile.domain.user_case

import com.madiwist.twitch.core.domain.models.UserItem
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.feature_profile.domain.repository.ProfileRepository

class SearchUserUseCase (
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(query: String) : Resource<List<UserItem>> {
        if (query.isBlank()){
            return Resource.Success(data = emptyList())
        }
        return repository.searchUser(query)
    }
}