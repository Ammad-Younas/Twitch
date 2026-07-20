package com.madiwist.twitch.feature_post.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.madiwist.twitch.core.domain.models.Post
import com.madiwist.twitch.core.util.SimpleResource
import kotlinx.coroutines.flow.Flow
import java.io.File

interface PostRepository {
    val posts : Flow<PagingData<Post>>

    suspend fun createPost(description: String, imageUri: Uri) : SimpleResource
}