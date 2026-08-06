package com.madiwist.twitch.feature_post.presentation.post_detail

import com.madiwist.twitch.core.domain.models.Comment
import com.madiwist.twitch.core.domain.models.Post

data class PostDetailsState(
    val post: Post? = null,
    val comments: List<Comment> = emptyList(),
    val isLoadingPost: Boolean = false,
    val isLoadingComments: Boolean = false,
)
