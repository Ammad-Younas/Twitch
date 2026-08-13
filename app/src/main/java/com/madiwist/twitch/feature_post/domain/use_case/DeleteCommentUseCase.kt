package com.madiwist.twitch.feature_post.domain.use_case

import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.feature_post.domain.repository.PostRepository

class DeleteCommentUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(commentId: String): SimpleResource {
        return repository.deleteComment(commentId)
    }
}
