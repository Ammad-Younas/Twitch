package com.madiwist.twitch.feature_profile.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.util.ParentType
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_post.domain.use_case.PostUseCases
import com.madiwist.twitch.feature_profile.domain.user_case.ProfileUserCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUserCases,
    private val postUseCases: PostUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _profileState = mutableStateOf(ProfileState())
    val profileState: State<ProfileState> = _profileState

    private val _toolBarOffsetY = mutableFloatStateOf(0f)
    val toolBarOffsetY: State<Float> = _toolBarOffsetY

    private val _expandedRatio = mutableFloatStateOf(1f)
    val expandedRatio: State<Float> = _expandedRatio

    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = 1)
    val eventFlow = _eventFlow.asSharedFlow()

    private val _userId = MutableStateFlow(savedStateHandle.get<String>("userId") ?: "")

    val postModifications = postUseCases.getPostModificationsUseCase()

    @OptIn(ExperimentalCoroutinesApi::class)
    val posts = _userId
        .filter { it.isNotEmpty() }
        .flatMapLatest { userId ->
            profileUseCase.getPosts(userId)
        }
        .cachedIn(viewModelScope)

    init {
        postUseCases.getPostCreatedEventUseCase()
            .onEach {
                val userId = _userId.value
                getProfile(userId)
                _eventFlow.emit(UiEvent.Refresh)
            }
            .launchIn(viewModelScope)

        postUseCases.getLikeUpdatedEventUseCase().onEach {}.launchIn(viewModelScope)
    }


    fun setExpandedRatio(ratio: Float) {
        _expandedRatio.floatValue = ratio
    }

    fun setToolbarOffsetY(value: Float) {
        _toolBarOffsetY.floatValue = value
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.GetProfile -> Unit
            is ProfileEvent.LikePost -> {
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
                toggleLikeForParent(post.id ?: "", isLiked)
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
                            uiText = result.uiText ?: UiText.unknownError()
                        )
                    )
                }
            }
        }
    }

    fun getProfile(userId: String) {
        viewModelScope.launch {
            _profileState.value = profileState.value.copy(isLoading = true)
            when (val result = profileUseCase.getProfile(userId)) {
                is Resource.Success -> {
                    val profile = result.data
                    _profileState.value = profileState.value.copy(
                        profile = profile,
                        isLoading = false
                    )
                    profile?.userId?.let { id ->
                        if (_userId.value != id) {
                            _userId.value = id
                        }
                    }
                }
                is Resource.Error -> {
                    _profileState.value = profileState.value.copy(isLoading = false)
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            uiText = result.uiText ?: UiText.unknownError()
                        )
                    )
                }
            }
        }
    }
}
