package com.madiwist.twitch.feature_post.domain.repository

import android.net.Uri
import com.madiwist.twitch.core.domain.models.Comment
import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.core.domain.models.UserItem
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.SimpleResource
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface PostRepository {

    suspend fun getPostsForFollows(page: Int, pageSize: Int): Resource<List<Post>>
    val onPostCreated: SharedFlow<Unit>
    val onLikeUpdated: SharedFlow<Unit>

    val postModifications: StateFlow<Map<String, Post>>
    val commentModifications: StateFlow<Map<String, Comment>>

    suspend fun createPost(description: String, imageUri: Uri) : SimpleResource
    suspend fun getPostDetails(postId: String) : Resource<Post>
    suspend fun getCommentsForPost(posId: String) : Resource<List<Comment>>
    suspend fun createComment(postId: String, comment: String) : SimpleResource
    suspend fun deleteComment(commentId: String) : SimpleResource
    suspend fun likeParent(parentId: String, parentType: Int) : SimpleResource
    suspend fun unlikeParent(parentId: String, parentType: Int) : SimpleResource
    suspend fun getLikesForParent(parentId: String) : Resource<List<UserItem>>

    fun updatePostModification(parentId: String, post: Post)
    fun abortPostModification(parentId: String)

    fun updateCommentModification(parentId: String, comment: Comment)
    fun abortCommentModification(parentId: String)
}
