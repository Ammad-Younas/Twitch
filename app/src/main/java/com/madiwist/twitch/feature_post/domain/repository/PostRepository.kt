package com.madiwist.twitch.feature_post.domain.repository

import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.core.util.Resource

interface PostRepository {
    suspend fun getPostsForFollows(page: Int = 0, pageSize: Int = Constants.PAGE_SIZE_POSTS) : Resource<List<Post>>
}