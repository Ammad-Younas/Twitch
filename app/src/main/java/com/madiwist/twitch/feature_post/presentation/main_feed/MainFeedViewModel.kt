package com.madiwist.twitch.feature_post.presentation.main_feed

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.util.ParentType
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_post.domain.use_case.PostUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFeedViewModel @Inject constructor(
    private val postUseCases: PostUseCases
) : ViewModel() {

    private val _mainfeedState = mutableStateOf(MainFeedState())
    val mainfeedState: State<MainFeedState> = _mainfeedState

    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = 1)
    val eventFlow = _eventFlow.asSharedFlow()

    val postModifications = postUseCases.getPostModificationsUseCase()

    val posts = postUseCases.getPostsForFollowsUseCase().cachedIn(viewModelScope)

    init {
        postUseCases.getPostCreatedEventUseCase()
            .onEach {
                _eventFlow.emit(UiEvent.Refresh)
            }
            .launchIn(viewModelScope)

        postUseCases.getLikeUpdatedEventUseCase().onEach {}.launchIn(viewModelScope)
    }

    fun onEvent(event: MainFeedEvent) {
        when(event){
            is MainFeedEvent.LoadMorePosts -> {
                _mainfeedState.value = mainfeedState.value.copy(
                    isLoadingNewPosts = true
                )
            }
            is MainFeedEvent.LoadedPage -> {
                _mainfeedState.value = mainfeedState.value.copy(
                    isLoadingFirstTime = false,
                    isLoadingNewPosts = false
                )
            }
            is MainFeedEvent.LikePost -> {
                val post = event.post
                val isLiked = post.isLiked == true
                val newLikeCount = if (isLiked) {
                    (post.likeCount ?: 0) - 1
                } else {
                    (post.likeCount ?: 0) + 1
                }
                val updatedPost = post.copy(
                    isLiked = !isLiked,
                    likeCount = newLikeCount
                )
                postUseCases.toggleLikeStateForParentUseCase.updatePostModification(post.id ?: "", updatedPost)
                toggleLikeForParent(post.id.orEmpty(), isLiked)
            }
        }
    }


    private fun toggleLikeForParent(
        parentId: String,
        isLiked: Boolean
    ) {
        viewModelScope.launch {
            val result = postUseCases.toggleLikeStateForParentUseCase(
                parentId = parentId,
                parentType = ParentType.Post.type,
                isLiked = isLiked
            )
            when (result) {
                is Resource.Success -> Unit
                is Resource.Error -> {
                    postUseCases.toggleLikeStateForParentUseCase.abortPostModification(parentId)
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
