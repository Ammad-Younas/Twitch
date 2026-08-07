package com.madiwist.twitch.feature_post.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.madiwist.twitch.feature_post.data.remote.PostApi
import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.core.util.Constants
import retrofit2.HttpException
import java.io.IOException

class PostSource (
    private val api: PostApi,
    private val source: Source
) : PagingSource<Int, Post>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            val nextPage = params.key ?: 0
            val posts = when (source) {
                is Source.Follows -> {
                    api.getPostsForFollows(
                        page = nextPage,
                        pageSize = Constants.DEFAULT_PAGE_SIZE
                    )
                }
                is Source.Profile -> {
                    api.getPostsForProfile(
                        userId = source.userId,
                        page = nextPage,
                        pageSize = Constants.DEFAULT_PAGE_SIZE
                    )
                }
            }
            LoadResult.Page(
                data = posts.map { it.toPost() },
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


    sealed class Source {
        object Follows : Source()
        data class Profile(val userId: String) : Source()
    }
}