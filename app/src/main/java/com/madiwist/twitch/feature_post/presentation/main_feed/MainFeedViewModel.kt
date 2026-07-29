package com.madiwist.twitch.feature_post.presentation.main_feed

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.feature_post.domain.use_case.PostUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainFeedViewModel @Inject constructor(
    postUseCases: PostUseCases
) : ViewModel() {

    private val _mainfeedState = mutableStateOf(MainFeedState())
    val mainfeedState: State<MainFeedState> = _mainfeedState

    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = 1)
    val eventFlow = _eventFlow.asSharedFlow()

    val posts = postUseCases.getPostsForFollowsUseCase().cachedIn(viewModelScope)

    init {
        postUseCases.getPostCreatedEventUseCase()
            .onEach {
                _eventFlow.emit(UiEvent.Refresh)
            }
            .launchIn(viewModelScope)
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
        }
    }
}