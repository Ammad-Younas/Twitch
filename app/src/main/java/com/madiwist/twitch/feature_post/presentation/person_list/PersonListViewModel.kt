package com.madiwist.twitch.feature_post.presentation.person_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_post.domain.use_case.PostUseCases
import com.madiwist.twitch.core.domain.use_case.ToggleFollowStateForUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonListViewModel @Inject constructor(
    private val postUseCases: PostUseCases,
    private val toggleFollowStateForUserUseCase: ToggleFollowStateForUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _usersState = mutableStateOf(PersonListState())
    val usersState: State<PersonListState> = _usersState

    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = 1)
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<String>("parentId")?.let { parentId->
            getLikesForParent(parentId = parentId)
        }
    }

    fun onEvent(event: PersonListEvent) {
        when (event) {
            is PersonListEvent.ToggleFollowState -> {
                toggleFollowStateForUser(event.userId)
            }
        }
    }

    private fun getLikesForParent(parentId: String) {
        viewModelScope.launch {
            _usersState.value = usersState.value.copy(
                isLoading = true
            )
            when(val result = postUseCases.getLikesForParentUseCase(parentId)){
                is Resource.Success -> {
                    _usersState.value = usersState.value.copy(
                        users = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _usersState.value = usersState.value.copy(
                        isLoading = false
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

    private fun toggleFollowStateForUser(userId: String) {
        viewModelScope.launch {
            val isFollowing = usersState.value.users.find { it.userId == userId }?.isFollowing == true

            _usersState.value = usersState.value.copy(
                users = usersState.value.users.map {
                    if (it.userId == userId) {
                        it.copy(isFollowing = !it.isFollowing)
                    } else {
                        it
                    }
                }
            )

            val result = toggleFollowStateForUserUseCase(
                userId = userId,
                isFollowing = isFollowing
            )
            when(result) {
                is Resource.Success -> Unit
                is Resource.Error -> {
                    _usersState.value = usersState.value.copy(
                        users = usersState.value.users.map {
                            if (it.userId == userId) {
                                it.copy(isFollowing = isFollowing)
                            } else {
                                it
                            }
                        }
                    )
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