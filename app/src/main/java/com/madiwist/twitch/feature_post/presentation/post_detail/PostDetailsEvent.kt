package com.madiwist.twitch.feature_post.presentation.post_detail

import com.madiwist.twitch.core.domain.models.Post

sealed class PostDetailsEvent {
    data class LikePost(val post: Post) : PostDetailsEvent()
    data class EnteredComment(val comment: String) : PostDetailsEvent()
    object Comment : PostDetailsEvent()
    data class LikeComment(val commentId: String) : PostDetailsEvent()
    object SharePost: PostDetailsEvent()
}