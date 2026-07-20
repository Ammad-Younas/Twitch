package com.madiwist.twitch.feature_post.domain.use_case

import android.net.Uri
import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.feature_post.domain.repository.PostRepository

class CreatePostUseCase (
    private val repository: PostRepository
) {
    suspend operator fun invoke(description : String, imageUri: Uri) : SimpleResource {
        return repository.createPost(description, imageUri)
    }
}