package com.madiwist.twitch.feature_profile.domain.user_case

import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.feature_profile.domain.repository.ProfileRepository

class GetPostsForProfileUserCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        userId: String,
        page: Int,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): Resource<List<Post>> {
        return repository.getPosts(userId, page, pageSize)
    }
}
