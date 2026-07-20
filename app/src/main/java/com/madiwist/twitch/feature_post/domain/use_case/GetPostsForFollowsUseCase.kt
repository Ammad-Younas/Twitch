package com.madiwist.twitch.feature_post.domain.use_case

import androidx.paging.PagingData
import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.feature_post.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class GetPostsForFollowsUseCase(
    private val repository: PostRepository
) {
    operator fun invoke() : Flow<PagingData<Post>> {
        return repository.posts
    }
}