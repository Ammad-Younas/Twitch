package com.madiwist.twitch.feature_post.data.repository

import com.madiwist.twitch.R
import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_post.data.data_source.remote.PostApi
import com.madiwist.twitch.feature_post.domain.repository.PostRepository
import okio.IOException
import retrofit2.HttpException

class PostRepositoryImpl (
    private val api: PostApi
) : PostRepository {
    override suspend fun getPostsForFollows(page: Int, pageSize: Int): Resource<List<Post>> {
        return try {
            val posts = api.getPostsForFollows(page = page, pageSize = pageSize)
                Resource.Success(posts)
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server),
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_something_went_wrong)
            )
        }
    }
}