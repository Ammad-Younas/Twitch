package com.madiwist.twitch.feature_search.presentation

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madiwist.twitch.core.domain.states.TwitchTextFieldState
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_profile.domain.user_case.ProfileUserCases
import com.madiwist.twitch.feature_profile.domain.util.ProfileConstant.SEARCH_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val profileUserCases: ProfileUserCases,
    sharedPreferences: SharedPreferences
): ViewModel() {
    
    private val _searchFieldState = mutableStateOf(TwitchTextFieldState())
    val searchFieldState: State<TwitchTextFieldState> = _searchFieldState
    
    private val _searchState = mutableStateOf(SearchState())
    val searchState: State<SearchState> = _searchState

    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = 1)
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null

    init {
        _searchState.value = searchState.value.copy(
            ownUserId = sharedPreferences.getString(Constants.KEY_USER_ID, "") ?: ""
        )
    }


    fun onEvent(event: SearchEvent){
        when (event) {
            is SearchEvent.Query -> {
                searchUser(event.query)
            }
            is SearchEvent.ToggleFollowState -> {
                toggleFollowStateForUser(event.userId)
            }
        }
    }


    private fun toggleFollowStateForUser(userId: String) {
        viewModelScope.launch {
            val isFollowing = searchState.value.userItems.find { it.userId == userId }?.isFollowing == true

            _searchState.value = searchState.value.copy(
                userItems = searchState.value.userItems.map {
                    if (it.userId == userId) {
                        it.copy(isFollowing = !it.isFollowing)
                    } else {
                        it
                    }
                }
            )

            val result = profileUserCases.toggleFollowStateForUser(
                userId = userId,
                isFollowing = isFollowing
            )
            when(result) {
                is Resource.Success -> Unit
                is Resource.Error -> {
                    _searchState.value = searchState.value.copy(
                        userItems = searchState.value.userItems.map {
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

    private fun searchUser(query: String) {
        _searchFieldState.value = searchFieldState.value.copy(
            text = query
        )
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY.milliseconds)
            _searchState.value = searchState.value.copy(isLoading = true)
            when (val result = profileUserCases.searchUser(query)) {
                is Resource.Success -> {
                    _searchState.value = searchState.value.copy(
                        userItems = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _searchState.value = searchState.value.copy(isLoading = false)
                    _searchFieldState.value = searchFieldState.value.copy(
                        error = SearchError(
                            result.uiText ?: UiText.unknownError()
                        )
                    )
                }
            }
        }
    }
}