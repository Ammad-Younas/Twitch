package com.madiwist.twitch.feature_post.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.madiwist.twitch.core.domain.models.Comment
import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.core.domain.models.UserItem
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.SimpleResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface PostRepository {

    val posts : Flow<PagingData<Post>>
    val onPostCreated: SharedFlow<Unit>
    val onLikeUpdated: SharedFlow<Unit>

    suspend fun createPost(description: String, imageUri: Uri) : SimpleResource
    suspend fun getPostDetails(postId: String) : Resource<Post>
    suspend fun getCommentsForPost(posId: String) : Resource<List<Comment>>
    suspend fun createComment(postId: String, comment: String) : SimpleResource
    suspend fun likeParent(parentId: String, parentType: Int) : SimpleResource
    suspend fun unlikeParent(parentId: String, parentType: Int) : SimpleResource
    suspend fun getLikesForParent(parentId: String) : Resource<List<UserItem>>
}