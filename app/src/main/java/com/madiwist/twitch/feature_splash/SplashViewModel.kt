package com.madiwist.twitch.feature_splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madiwist.twitch.core.presentation.navigation.Screen
import com.madiwist.twitch.core.presentation.util.UiEvent
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.feature_auth.domain.use_case.AuthenticateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authenticateUseCase: AuthenticateUseCase
) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun authenticate() {
        viewModelScope.launch {
            val result = authenticateUseCase()
            delay(Constants.SPLASH_SCREEN_DURATION.milliseconds)
            when(result) {
                is Resource.Success -> {
                    _eventFlow.emit(
                        UiEvent.Navigate(Screen.MainFeedScreen.route)
                    )
                }
                is Resource.Error -> {
                    _eventFlow.emit(
                        UiEvent.Navigate(Screen.LoginScreen.route)
                    )
                }
            }
        }
    }
}







