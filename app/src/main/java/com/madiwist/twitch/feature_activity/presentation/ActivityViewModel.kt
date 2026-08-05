package com.madiwist.twitch.feature_activity.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.madiwist.twitch.core.presentation.navigation.Screen
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.feature_activity.domain.use_case.GetActivityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    getActivities: GetActivityUseCase
) : ViewModel() {

    private val _activityState = mutableStateOf(ActivityState())
    val activityState: State<ActivityState> = _activityState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val activities = getActivities().cachedIn(viewModelScope)

    fun onEvent(event: ActivityEvent) {
        when (event) {
            is ActivityEvent.ClickedOnUser -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.Navigate(
                        Screen.ProfileScreen.route + "?userId=${event.userId}"
                    ))
                }
            }

            is ActivityEvent.ClickedOnParent -> {
                viewModelScope.launch {
                    // For now navigating to PostDetails. 
                    // In a real app, you might check if it's a post or comment ID.
                    _eventFlow.emit(UiEvent.Navigate(Screen.PostDetailsScreen.route))
                }
            }
        }
    }
}