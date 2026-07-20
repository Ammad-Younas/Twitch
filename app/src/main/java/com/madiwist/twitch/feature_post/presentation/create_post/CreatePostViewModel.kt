package com.madiwist.twitch.feature_post.presentation.create_post

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.madiwist.twitch.core.domain.states.TwitchTextFieldState
import com.madiwist.twitch.feature_post.domain.use_case.PostUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val postUseCases: PostUseCases,
    application: Application,
) : AndroidViewModel(application) {

    private val _descriptionState = mutableStateOf(TwitchTextFieldState())
    val descriptionState: State<TwitchTextFieldState> = _descriptionState
    private val _croppedBitmap = mutableStateOf<Bitmap?>(null)
    val croppedBitmap: State<Bitmap?> = _croppedBitmap

    fun onEvent(event: CreatePostEvent) {
        when (event) {
            is CreatePostEvent.EnterDescription -> {
                _descriptionState.value = descriptionState.value.copy(text = event.value)
            }
            is CreatePostEvent.PickImage -> Unit
            is CreatePostEvent.CropImage -> {
                _croppedBitmap.value = event.bitmap
            }
            is CreatePostEvent.PostImage -> {
                val bitmap = croppedBitmap.value ?: return
                viewModelScope.launch {
                    val imageUri: Uri = withContext(Dispatchers.IO) {
                        saveBitmapToCache(bitmap)
                    }
                    postUseCases.createPostUseCase(
                        description = descriptionState.value.text,
                        imageUri = imageUri,
                    )
                }
            }
        }
    }
    private fun saveBitmapToCache(bitmap: Bitmap): Uri {
        val cacheDir = getApplication<Application>().cacheDir
        val file     = File(cacheDir, "post_image_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        return file.toUri()
    }
}