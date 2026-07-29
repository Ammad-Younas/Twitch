package com.madiwist.twitch.feature_post.domain.use_case

import com.madiwist.twitch.feature_post.domain.repository.PostRepository
import kotlinx.coroutines.flow.SharedFlow

class GetPostCreatedEventUseCase(
    private val repository: PostRepository
) {
    operator fun invoke(): SharedFlow<Unit> {
        return repository.onPostCreated
    }
}
