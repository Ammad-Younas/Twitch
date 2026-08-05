package com.madiwist.twitch.feature_post.presentation.post_detail

import com.madiwist.twitch.core.domain.models.Post

data class PostDetailsState(
    val post: Post? = null,
    val isLoadingPost: Boolean = false,
)
