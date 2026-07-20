package com.madiwist.twitch.feature_post.presentation.create_post

import android.graphics.Bitmap
import android.net.Uri

sealed class CreatePostEvent {
    data class EnterDescription(val value: String) : CreatePostEvent()
    data class PickImage(val uri: Uri?) : CreatePostEvent()
    data class CropImage(val bitmap: Bitmap?) : CreatePostEvent()
    object PostImage : CreatePostEvent()
}