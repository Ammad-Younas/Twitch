package com.madiwist.twitch.feature_post.presentation.main_feed

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.madiwist.twitch.feature_post.domain.use_case.PostUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFeedViewModel @Inject constructor(
    postUseCases: PostUseCases
) : ViewModel() {

    private val _mainfeedState = mutableStateOf(MainFeedState())
    val mainfeedState: State<MainFeedState> = _mainfeedState

    val posts = postUseCases.getPostsForFollowsUseCase().cachedIn(viewModelScope)

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