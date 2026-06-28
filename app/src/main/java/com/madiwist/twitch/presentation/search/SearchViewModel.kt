package com.madiwist.twitch.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.madiwist.twitch.presentation.utils.states.TwitchTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(): ViewModel() {
    
    private val _searchState = mutableStateOf(TwitchTextFieldState())
    val searchState: State<TwitchTextFieldState> = _searchState

    fun setSearchState(state: TwitchTextFieldState) {
        _searchState.value = state
    }
}