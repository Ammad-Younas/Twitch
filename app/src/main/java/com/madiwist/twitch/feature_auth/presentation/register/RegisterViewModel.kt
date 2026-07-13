package com.madiwist.twitch.feature_auth.presentation.register

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madiwist.twitch.R
import com.madiwist.twitch.core.domain.states.PasswordTextFiledState
import com.madiwist.twitch.core.domain.states.TwitchTextFieldState
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.core.util.Resource
import com.madiwist.twitch.core.util.UiText
import com.madiwist.twitch.feature_auth.domain.use_case.RegisterUserCase
import com.madiwist.twitch.feature_auth.presentation.util.AuthError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
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
                _usernameState.value = _usernameState.value.copy(
                    text = event.value
                )
            }
            is RegisterEvent.EnteredEmail -> {
                _emailState.value = _emailState.value.copy(
                    text = event.value
                )
            }
            is RegisterEvent.EnteredPassword -> {
                _passwordState.value = _passwordState.value.copy(
                    text = event.value
                )
            }
            is RegisterEvent.TogglePasswordVisibility -> {
                _passwordState.value = _passwordState.value.copy(
                    isPasswordVisible = !_passwordState.value.isPasswordVisible
                )
            }
            is RegisterEvent.Register -> {
                validateUsername(usernameState.value.text)
                validateEmail(emailState.value.text)
                validatePassword(passwordState.value.text)
                registerIfNoErrors()
            }
        }
    }

    private fun registerIfNoErrors(){
        if (
            usernameState.value.error == null
            ||
            emailState.value.error == null
            ||
            passwordState.value.error == null
        ) {
            return
        }
        viewModelScope.launch {
            _registerState.value = RegisterState(isLoading = true)
            val result = registerUserCase(
                email = emailState.value.text,
                username = usernameState.value.text,
                password = passwordState.value.text
            )
            when (result) {
                is Resource.Success -> {
                    _eventFlow.emit(
                        UiEvent.SnackbarEvent(UiText.StringResource(R.string.successfully_registered))
                    )
                }
                is Resource.Error -> {
                    _eventFlow.emit(
                        UiEvent.SnackbarEvent(result.uiText ?: UiText.unknownError())
                    )
                }
            }
        }
    }

    private fun validateUsername(username: String){
        val trimmedUsername = username.trim()
        if (trimmedUsername.isBlank()){
            _usernameState.value = _usernameState.value.copy(
                error = AuthError.FieldEmpty
            )
            return
        }
        if (trimmedUsername.length < Constants.MIN_USERNAME_LENGTH){
            _usernameState.value = _usernameState.value.copy(
                error = AuthError.InputTooShort
            )
            return
        }
        _usernameState.value = _usernameState.value.copy(
            error = null
        )
    }


    private fun validateEmail(email: String){
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank()){
            _emailState.value = _emailState.value.copy(
                error = AuthError.FieldEmpty
            )
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            _emailState.value = _emailState.value.copy(
                error = AuthError.InvalidEmail
            )
            return
        }
        _emailState.value = _emailState.value.copy(
            error = null
        )
    }

    private fun validatePassword(password: String){
        if (password.isBlank()){
            _passwordState.value = _passwordState.value.copy(
                error = AuthError.FieldEmpty
            )
            return
        }
        if (password.length < Constants.MIN_PASSWORD_LENGTH){
            _passwordState.value = _passwordState.value.copy(
                error = AuthError.InputTooShort
            )
            return
        }
        val capitalLettersInPassword = password.any { it.isUpperCase() }
        val numbersInPassword = password.any { it.isDigit() }
        if (!capitalLettersInPassword || !numbersInPassword) {
            _passwordState.value = _passwordState.value.copy(
                error = AuthError.InvalidPassword
            )
            return
        }
        _passwordState.value = _passwordState.value.copy(
            error = null
        )
    }

    sealed class UiEvent {
        data class SnackbarEvent(val uiText: UiText): UiEvent()
    }
}
