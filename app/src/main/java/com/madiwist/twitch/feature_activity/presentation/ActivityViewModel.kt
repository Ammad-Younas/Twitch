package com.madiwist.twitch.feature_activity.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.madiwist.twitch.feature_activity.domain.use_case.GetActivityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    getActivities: GetActivityUseCase
) : ViewModel() {

    private val _activityState = mutableStateOf(ActivityState())
    val activityState: State<ActivityState> = _activityState

    val activities = getActivities().cachedIn(viewModelScope)

    fun onEvent(event: ActivityEvent) {
        when (event) {
            is ActivityEvent.ClickedOnUser -> {

            }

            is ActivityEvent.ClickedOnParent -> {

            }
        }
    }
}