package com.madiwist.twitch.feature_post.domain.use_case

import android.net.Uri
import com.madiwist.twitch.R
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.SimpleResource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_post.domain.repository.PostRepository

class CreatePostUseCase (
    private val repository: PostRepository
) {
    suspend operator fun invoke(description : String, imageUri: Uri?) : SimpleResource {
        if (imageUri == null) {
            return Resource.Error(uiText = UiText.StringResource(R.string.error_no_image_provided))
        }
        if (description.isBlank()) {
            return Resource.Error(uiText = UiText.StringResource(R.string.error_description_blank))
        }
        return repository.createPost(description, imageUri)
    }
}