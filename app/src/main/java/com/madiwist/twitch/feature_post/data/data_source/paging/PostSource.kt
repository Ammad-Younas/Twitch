package com.madiwist.twitch.feature_post.data.data_source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.feature_post.data.data_source.remote.PostApi
import retrofit2.HttpException
import java.io.IOException

class PostSource (
    private val api: PostApi
) : PagingSource<Int, Post>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            val nextPage = params.key ?: 0
            val posts = api.getPostsForFollows(
                page = nextPage,
                pageSize = Constants.PAGE_SIZE_POSTS
            )
            LoadResult.Page(
                data = posts,
                prevKey = if (nextPage == 0) null else nextPage - 1,
                nextKey = if (posts.isEmpty()) null else nextPage + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition
    }
}