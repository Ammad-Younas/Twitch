package com.madiwist.twitch.feature_post.domain.use_case

import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.feature_post.domain.repository.PostRepository

class GetPostsForFollowsUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
        page: Int,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ) : Resource<List<Post>> {
        return repository.getPostsForFollows(page, pageSize)
    }
}
