package com.madiwist.twitch.feature_post.domain.use_case

import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_post.domain.repository.PostRepository

class CreateCommentUseCase (
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: String, comment: String) : SimpleResource {
        if (postId.isBlank()){
            return Resource.Error(UiText.unknownError())
        }
        return repository.createComment(postId = postId, comment = comment)
    }
}