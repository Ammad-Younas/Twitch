package com.madiwist.twitch.feature_post.domain.use_case

import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.feature_post.domain.repository.PostRepository
import com.madiwist.twitch.feature_profile.domain.repository.ProfileRepository

class ToggleLikeStateForParentUseCase (
    private val repository: PostRepository
) {
    suspend operator fun invoke(parentId: String, parentType: Int, isLiked: Boolean) : SimpleResource {
        return if (isLiked){
            repository.unlikeParent(parentId = parentId, parentType = parentType)
        } else {
            repository.likeParent(parentId = parentId, parentType = parentType)
        }
    }
}