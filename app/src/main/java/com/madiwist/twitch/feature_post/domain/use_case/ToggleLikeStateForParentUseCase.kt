package com.madiwist.twitch.feature_post.domain.use_case

import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.feature_post.domain.repository.PostRepository

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

    fun updatePostModification(parentId: String, post: Post) {
        repository.updatePostModification(parentId, post)
    }

    fun abortPostModification(parentId: String) {
        repository.abortPostModification(parentId)
    }
}
