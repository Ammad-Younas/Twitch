package com.madiwist.twitch.feature_activity.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madiwist.twitch.core.presentation.navigation.Screen
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.core.util.paging.DefaultPaginator
import com.madiwist.twitch.feature_activity.domain.use_case.GetActivityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val getActivityUseCase: GetActivityUseCase
) : ViewModel() {

    private val _activityState = mutableStateOf(ActivityState())
    val activityState: State<ActivityState> = _activityState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val paginator = DefaultPaginator(
        initialKey = _activityState.value.page,
        onLoadUpdated = { isLoading ->
            _activityState.value = _activityState.value.copy(isLoading = isLoading)
        },
        onRequest = { nextPage ->
            getActivityUseCase(page = nextPage)
        },
        getNextKey = { items ->
            _activityState.value.page + 1
        },
        onError = { uiText ->
            _eventFlow.emit(UiEvent.ShowSnackBar(uiText ?: UiText.unknownError()))
        },
        onSuccess = { items, newKey ->
            _activityState.value = _activityState.value.copy(
                activities = _activityState.value.activities + items,
                endReached = items.isEmpty(),
                page = newKey
            )
        }
    )

    init {
        loadNextActivities()
    }

    fun loadNextActivities() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }

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
                    _eventFlow.emit(UiEvent.Navigate(Screen.PostDetailsScreen.route))
                }
            }
        }
    }
}
