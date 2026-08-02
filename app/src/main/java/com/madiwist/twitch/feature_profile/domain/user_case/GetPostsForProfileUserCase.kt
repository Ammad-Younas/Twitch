package com.madiwist.twitch.feature_profile.domain.user_case

import androidx.paging.PagingData
import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetPostsForProfileUserCase(
    private val repository: ProfileRepository
) {
    operator fun invoke(userId: String): Flow<PagingData<Post>> {
        return repository.getPostsPaged(userId)
    }
}
