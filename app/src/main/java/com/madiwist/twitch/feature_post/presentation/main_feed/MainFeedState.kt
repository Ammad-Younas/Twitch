package com.madiwist.twitch.feature_post.presentation.main_feed

import com.madiwist.twitch.core.domain.models.Post

data class MainFeedState(
    val posts: List<Post> = emptyList(),
    val isLoadingFirstTime: Boolean = true,
    val isLoadingNewPosts: Boolean = false,
    val isRefreshing: Boolean = false,
    val endReached: Boolean = false,
    val page: Int = 0
)
