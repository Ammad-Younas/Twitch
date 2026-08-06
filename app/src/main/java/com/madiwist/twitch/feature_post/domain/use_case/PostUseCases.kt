package com.madiwist.twitch.feature_post.domain.use_case

data class PostUseCases (
    val getPostsForFollowsUseCase: GetPostsForFollowsUseCase,
    val createPostUseCase: CreatePostUseCase,
    val getPostCreatedEventUseCase: GetPostCreatedEventUseCase,
    val getPostDetailsUseCase: GetPostDetailsUseCase,
    val getCommentsForPostUseCase: GetCommentsForPostUseCase,
    val createCommentUseCase: CreateCommentUseCase
)