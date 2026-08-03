package com.madiwist.twitch.feature_search.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madiwist.twitch.core.domain.states.TwitchTextFieldState
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_profile.domain.user_case.ProfileUserCases
import com.madiwist.twitch.feature_profile.domain.util.ProfileConstant.SEARCH_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val profileUserCases: ProfileUserCases
): ViewModel() {
    
    private val _searchFieldState = mutableStateOf(TwitchTextFieldState())
    val searchFieldState: State<TwitchTextFieldState> = _searchFieldState
    
    private val _searchState = mutableStateOf(SearchState())
    val searchState: State<SearchState> = _searchState

    private var searchJob: Job? = null

    fun onEvent(event: SearchEvent){
        when (event) {
            is SearchEvent.Query -> {
                searchUser(event.query)
            }
        }
    }

    private fun searchUser(query: String) {
        _searchFieldState.value = searchFieldState.value.copy(
            text = query
        )
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