package com.madiwist.twitch.feature_post.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.feature_post.data.data_source.paging.PostSource
import com.madiwist.twitch.feature_post.data.data_source.remote.PostApi
import com.madiwist.twitch.feature_post.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class PostRepositoryImpl (
    private val api: PostApi
) : PostRepository {
    override val posts: Flow<PagingData<Post>>
        get() = Pager(PagingConfig(pageSize = Constants.PAGE_SIZE_POSTS)) {
            PostSource(api)
        }.flow
}