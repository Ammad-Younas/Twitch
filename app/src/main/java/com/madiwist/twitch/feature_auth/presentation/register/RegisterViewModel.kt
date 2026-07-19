package com.madiwist.twitch.feature_auth.presentation.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madiwist.twitch.R
import com.madiwist.twitch.core.domain.states.PasswordTextFiledState
import com.madiwist.twitch.core.domain.states.TwitchTextFieldState
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_auth.domain.use_case.RegisterUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import com.madiwist.twitch.core.presentation.util.UiEvent
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserCase: RegisterUserCase
)  : ViewModel() {

    private val _usernameState = mutableStateOf(TwitchTextFieldState())
    val usernameState : State<TwitchTextFieldState> = _usernameState

    private val _emailState = mutableStateOf(TwitchTextFieldState())
    val emailState : State<TwitchTextFieldState> = _emailState

    private val _passwordState = mutableStateOf(PasswordTextFiledState())
    val passwordState : State<PasswordTextFiledState> = _passwordState

    private val _registerState = mutableStateOf(RegisterState())
    val registerState : State<RegisterState> = _registerState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: RegisterEvent){
        when(event){
            is RegisterEvent.EnteredUsername -> {
                _usernameState.value = usernameState.value.copy(
                    text = event.value
                )
            }
            is RegisterEvent.EnteredEmail -> {
                _emailState.value = emailState.value.copy(
                    text = event.value
                )
            }
            is RegisterEvent.EnteredPassword -> {
                _passwordState.value = passwordState.value.copy(
                    text = event.value
                )
            }
            is RegisterEvent.TogglePasswordVisibility -> {
                _passwordState.value = passwordState.value.copy(
                    isPasswordVisible = !passwordState.value.isPasswordVisible
                )
            }
            is RegisterEvent.Register -> {
                register()
            }
        }
    }

    private fun register(){
        viewModelScope.launch {
            _usernameState.value = usernameState.value.copy(error = null)
            _emailState.value = emailState.value.copy(error = null)
            _passwordState.value = passwordState.value.copy(error = null)
            _registerState.value = RegisterState(isLoading = true)
            val registerResult = registerUserCase(
                email = emailState.value.text,
                username = usernameState.value.text,
                password = passwordState.value.text
            )
            if (registerResult.emailError != null){
                _emailState.value = emailState.value.copy(
                    error = registerResult.emailError
                )
            }
            if (registerResult.usernameError != null){
                _usernameState.value = usernameState.value.copy(
                    error = registerResult.usernameError
                )
            }
            if (registerResult.passwordError != null){
                _passwordState.value = passwordState.value.copy(
                    error = registerResult.passwordError
                )
            }
            when (registerResult.result) {
                is Resource.Success -> {
                    _eventFlow.emit(
                        UiEvent.SnackbarEvent(UiText.StringResource(R.string.successfully_registered))
                    )
                    _registerState.value = RegisterState(isLoading = false)
                    _usernameState.value = TwitchTextFieldState()
                    _emailState.value = TwitchTextFieldState()
                    _passwordState.value = PasswordTextFiledState()
                }
                is Resource.Error -> {
                    _eventFlow.emit(
                        UiEvent.SnackbarEvent(registerResult.result.uiText ?: UiText.unknownError())
                    )
                    _registerState.value = RegisterState(isLoading = false)
                }
                null -> {
                    _registerState.value = RegisterState(isLoading = false)
                }
            }
        }
    }
}
