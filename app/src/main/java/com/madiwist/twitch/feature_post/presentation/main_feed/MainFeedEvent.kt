package com.madiwist.twitch.feature_post.presentation.main_feed

import com.madiwist.twitch.core.domain.models.Post

sealed class MainFeedEvent {
    object LoadMorePosts: MainFeedEvent()
    object LoadedPage: MainFeedEvent()
    data class LikePost(val post: Post) : MainFeedEvent()
}