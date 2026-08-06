package com.madiwist.twitch.feature_post.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.madiwist.twitch.core.domain.models.Comment
import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.SimpleResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface PostRepository {

    val posts : Flow<PagingData<Post>>
    val onPostCreated: SharedFlow<Unit>

    suspend fun createPost(description: String, imageUri: Uri) : SimpleResource
    suspend fun getPostDetails(postId: String) : Resource<Post>
    suspend fun getCommentsForPost(posId: String) : Resource<List<Comment>>
    suspend fun createComment(postId: String, comment: String) : SimpleResource
}