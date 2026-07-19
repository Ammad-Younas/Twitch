package com.madiwist.twitch.feature_post.presentation.main_feed

import com.madiwist.twitch.core.domain.models.Post

data class MainFeedState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val page: Int = 0
)
