package com.madiwist.twitch.feature_post.presentation.create_post

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.madiwist.twitch.R
import com.madiwist.twitch.core.domain.states.TwitchTextFieldState
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_post.domain.use_case.CreatePostState
import com.madiwist.twitch.feature_post.domain.use_case.PostUseCases
import com.madiwist.twitch.feature_post.presentation.util.PostConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val postUseCases: PostUseCases,
    application: Application,
) : AndroidViewModel(application) {

    private val _descriptionState = mutableStateOf(TwitchTextFieldState())
    val descriptionState: State<TwitchTextFieldState> = _descriptionState

    private val _croppedBitmap = mutableStateOf<Bitmap?>(null)
    val croppedBitmap: State<Bitmap?> = _croppedBitmap

    private val _createPostState = mutableStateOf(CreatePostState())
    val createPostState: State<CreatePostState> = _createPostState

    private val _eventFlow = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val eventFlow = _eventFlow.asSharedFlow()

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
                val currentBitmap = croppedBitmap.value
                val currentDescription = descriptionState.value.text
                if (currentBitmap == null) {
                    _eventFlow.tryEmit(
                        UiEvent.SnackbarEvent(UiText.StringResource(R.string.error_no_image_provided))
                    )
                    return
                }
                if (currentDescription.isBlank()) {
                    _eventFlow.tryEmit(
                        UiEvent.SnackbarEvent(UiText.StringResource(R.string.error_description_blank))
                    )
                    return
                }
                viewModelScope.launch {
                    _createPostState.value = CreatePostState(isLoading = true)
                    val startTime = System.currentTimeMillis()

                    val imageUri: Uri = withContext(Dispatchers.IO) {
                        saveBitmapToCache(currentBitmap)
                    }
                    val result = postUseCases.createPostUseCase(
                        description = currentDescription,
                        imageUri = imageUri,
                    )
                    val elapsed = System.currentTimeMillis() - startTime
                    if (elapsed < PostConstants.MIN_LOADING_DURATION_MS) {
                        delay((PostConstants.MIN_LOADING_DURATION_MS - elapsed).milliseconds)
                    }
                    _createPostState.value = CreatePostState(isLoading = false)
                    when (result) {
                        is Resource.Success -> {
                            _eventFlow.emit(UiEvent.SnackbarEvent(UiText.StringResource(R.string.post_published)))
                            _eventFlow.emit(UiEvent.NavigateUp)
                        }
                        is Resource.Error -> {
                            _eventFlow.emit(
                                UiEvent.SnackbarEvent(
                                    uiText = result.uiText ?: UiText.unknownError()
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun saveBitmapToCache(bitmap: Bitmap): Uri {
        val cacheDir = getApplication<Application>().cacheDir
        val file = File(cacheDir, "post_image_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        return file.toUri()
    }
}