package com.madiwist.twitch.feature_post.presentation.post_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_post.domain.use_case.PostUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val postUseCases: PostUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _postDetailsState = mutableStateOf(PostDetailsState())
    val postDetailsState: State<PostDetailsState> = _postDetailsState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<String>("postId")?.let { postId ->
            loadPostDetails(postId)
            loadCommentsForPost(postId)
        }
    }


    fun onEvent(event: PostDetailsEvent) {
        when (event) {
            is PostDetailsEvent.LikePost -> {
                TODO()
            }

            is PostDetailsEvent.Comment -> {
                TODO()
            }

            is PostDetailsEvent.LikeComment -> {
                TODO()
            }

            is PostDetailsEvent.SharePost -> {
                TODO()
            }
        }
    }

    private fun loadPostDetails(postId: String){
        viewModelScope.launch {
            _postDetailsState.value = postDetailsState.value.copy(isLoadingPost = true)
            when(val result = postUseCases.getPostDetailsUseCase(postId)){
                is Resource.Success -> {
                    _postDetailsState.value = postDetailsState.value.copy(
                        post = result.data,
                        isLoadingPost = false
                    )
                }
                is Resource.Error -> {
                    _postDetailsState.value = postDetailsState.value.copy(
                        isLoadingPost = false
                    )
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            result.uiText ?: UiText.unknownError()
                        )
                    )
                }
            }
        }
    }

    private fun loadCommentsForPost(postId: String){
        viewModelScope.launch {
            _postDetailsState.value = postDetailsState.value.copy(isLoadingPost = true)
            when(val result = postUseCases.getCommentsForPostUseCase(postId)){
                is Resource.Success -> {
                    _postDetailsState.value = postDetailsState.value.copy(
                        comments = result.data ?: emptyList(),
                        isLoadingComments = false
                    )
                }
                is Resource.Error -> {
                    _postDetailsState.value = postDetailsState.value.copy(
                        isLoadingComments = false
                    )
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            result.uiText ?: UiText.unknownError()
                        )
                    )
                }
            }
        }
    }
}