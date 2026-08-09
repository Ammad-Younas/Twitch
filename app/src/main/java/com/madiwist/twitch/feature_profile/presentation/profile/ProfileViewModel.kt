package com.madiwist.twitch.feature_profile.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.util.DefaultPaginator
import com.madiwist.twitch.core.util.ParentType
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_post.domain.use_case.PostUseCases
import com.madiwist.twitch.feature_profile.domain.user_case.ProfileUserCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val paginator = DefaultPaginator(
        initialKey = _profileState.value.page,
        onLoadUpdated = { isLoading ->
            _profileState.value = _profileState.value.copy(
                isLoading = isLoading
            )
        },
        onRequest = { nextPage ->
            profileUseCase.getPosts(_userId.value, nextPage)
        },
        getNextKey = {
            _profileState.value.page + 1
        },
        onError = { uiText ->
            _eventFlow.emit(UiEvent.ShowSnackBar(uiText ?: UiText.unknownError()))
        },
        onSuccess = { items, newKey ->
            _profileState.value = _profileState.value.copy(
                posts = _profileState.value.posts + items,
                endReached = items.isEmpty(),
                page = newKey
            )
        }
    )

    init {
        postUseCases.getPostCreatedEventUseCase()
            .onEach {
                val currentUserId = _userId.value
                getProfile(currentUserId)
                refresh()
                _eventFlow.emit(UiEvent.Refresh)
            }
            .launchIn(viewModelScope)

        postUseCases.getLikeUpdatedEventUseCase().onEach {}.launchIn(viewModelScope)
    }

    fun loadNextPosts() {
        if (_userId.value.isEmpty()) return
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }

    private fun refresh() {
        paginator.reset()
        _profileState.value = _profileState.value.copy(
            posts = emptyList(),
            page = 0,
            endReached = false
        )
        loadNextPosts()
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
                            refresh()
                        } else if (_profileState.value.posts.isEmpty()) {
                            loadNextPosts()
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
