package com.madiwist.twitch.feature_post.domain.use_case

import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.feature_post.domain.repository.PostRepository

class GetPostDetailsUseCase (
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: String) : Resource<Post> {
        return repository.getPostDetails(postId)
    }
}