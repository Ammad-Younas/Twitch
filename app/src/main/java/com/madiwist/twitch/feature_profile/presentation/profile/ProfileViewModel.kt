package com.madiwist.twitch.feature_profile.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_profile.domain.user_case.ProfileUserCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUserCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _profileState = mutableStateOf(ProfileState())
    val profileState: State<ProfileState> = _profileState

    private val _toolBarOffsetY = mutableFloatStateOf(0f)
    val toolBarOffsetY: State<Float> = _toolBarOffsetY

    private val _expandedRatio = mutableFloatStateOf(1f)
    val expandedRatio: State<Float> = _expandedRatio

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        val userId = savedStateHandle.get<String>("userId") ?: ""
        getProfile(userId)
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
        }
    }

    private fun getProfile(userId: String) {
        viewModelScope.launch {
            _profileState.value = profileState.value.copy(isLoading = true)
            when (val result = profileUseCase.getProfile(userId)) {
                is Resource.Success -> {
                    _profileState.value = profileState.value.copy(
                        profile = result.data,
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _profileState.value = profileState.value.copy(isLoading = false)
                    _eventFlow.emit(
                        UiEvent.SnackbarEvent(
                            uiText = result.uiText ?: UiText.unknownError()
                        )
                    )
                }
            }
        }
    }
}