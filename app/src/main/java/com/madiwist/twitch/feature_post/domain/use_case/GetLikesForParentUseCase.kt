package com.madiwist.twitch.feature_post.domain.use_case

import com.madiwist.twitch.core.domain.models.UserItem
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.feature_post.domain.repository.PostRepository

class GetLikesForParentUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(parentId: String) : Resource<List<UserItem>> {
        return repository.getLikesForParent(parentId)
    }
}