package com.madiwist.twitch.feature_post.domain.use_case

import com.madiwist.twitch.core.domain.models.Comment
import com.madiwist.twitch.feature_post.domain.repository.PostRepository
import kotlinx.coroutines.flow.StateFlow

class GetCommentModificationsUseCase(
    private val repository: PostRepository
) {
    operator fun invoke(): StateFlow<Map<String, Comment>> {
        return repository.commentModifications
    }
}
