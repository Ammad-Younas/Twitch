package com.madiwist.twitch.feature_post.domain.use_case

import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.feature_post.domain.repository.PostRepository
import kotlinx.coroutines.flow.StateFlow

class GetPostModificationsUseCase(
    private val repository: PostRepository
) {
    operator fun invoke(): StateFlow<Map<String, Post>> {
        return repository.postModifications
    }
}
