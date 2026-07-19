package com.madiwist.twitch.feature_auth.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madiwist.twitch.core.domain.states.PasswordTextFiledState
import com.madiwist.twitch.core.domain.states.TwitchTextFieldState
import com.madiwist.twitch.core.presentation.navigation.Screen
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_auth.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
)  : ViewModel() {

    private val _emailState = mutableStateOf(TwitchTextFieldState())
    val emailState : State<TwitchTextFieldState> = _emailState

    private val _passwordState = mutableStateOf(PasswordTextFiledState())
    val passwordState : State<PasswordTextFiledState> = _passwordState

    private val _loginState = mutableStateOf(LoginState())
    val loginState : State<LoginState> = _loginState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.EnteredEmail -> {
                _emailState.value = emailState.value.copy(
                    text = event.value
                )
            }
            is LoginEvent.EnteredPassword -> {
                _passwordState.value = passwordState.value.copy(
                    text = event.value
                )
            }
            is LoginEvent.TogglePasswordVisibility -> {
                _passwordState.value = passwordState.value.copy(
                    isPasswordVisible = !passwordState.value.isPasswordVisible
                )
            }
            is LoginEvent.Login -> {
                login()
            }
        }
    }

    private fun login(){
        viewModelScope.launch {
            _emailState.value = emailState.value.copy(error = null)
            _passwordState.value = passwordState.value.copy(error = null)
            _loginState.value = LoginState(isLoading = true)
            val loginResult = loginUseCase(
                email = emailState.value.text,
                password = passwordState.value.text
            )
            if (loginResult.emailError != null){
                _emailState.value = emailState.value.copy(
                    error = loginResult.emailError
                )
            }
            if (loginResult.passwordError != null){
                _passwordState.value = passwordState.value.copy(
                    error = loginResult.passwordError
                )
            }
            when (loginResult.result){
                is Resource.Success -> {
                    _eventFlow.emit(
                        UiEvent.Navigate(Screen.MainFeedScreen.route),
                    )
                    _loginState.value = LoginState(isLoading = false)
                    _emailState.value = TwitchTextFieldState()
                    _passwordState.value = PasswordTextFiledState()
                }
                is Resource.Error -> {
                    _eventFlow.emit(
                        UiEvent.SnackbarEvent(loginResult.result.uiText ?: UiText.unknownError())
                    )
                    _loginState.value = LoginState(isLoading = false)
                }
                null -> {
                    _loginState.value = LoginState(isLoading = false)
                }
            }
        }
    }


    sealed class UiEvent {
        data class SnackbarEvent(val uiText: UiText): UiEvent()
        data class Navigate(val route: String): UiEvent()
    }
}