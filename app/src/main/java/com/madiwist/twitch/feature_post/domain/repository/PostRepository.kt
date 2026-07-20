package com.madiwist.twitch.feature_post.domain.repository

import androidx.paging.PagingData
import com.madiwist.twitch.core.domain.models.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    val posts : Flow<PagingData<Post>>
}