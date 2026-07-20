package com.madiwist.twitch.feature_splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madiwist.twitch.core.presentation.navigation.Screen
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.feature_auth.domain.use_case.AuthenticateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authenticateUseCase: AuthenticateUseCase
) : ViewModel() {

    private val _isAuthComplete = MutableStateFlow(false)
    val isAuthComplete: StateFlow<Boolean> = _isAuthComplete.asStateFlow()

    private val _authDestination = MutableStateFlow<String?>(null)
    val authDestination: StateFlow<String?> = _authDestination.asStateFlow()

    fun authenticate() {
        viewModelScope.launch {
            val result = authenticateUseCase()
            _authDestination.value = when (result) {
                is Resource.Success -> Screen.MainFeedScreen.route
                is Resource.Error   -> Screen.LoginScreen.route
            }
            _isAuthComplete.value = true
        }
    }
}
