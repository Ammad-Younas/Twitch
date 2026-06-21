package com.madiwist.twitch.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class LoginViewModel @Inject constructor()  : ViewModel() {
    private val _username = mutableStateOf("")
    val username: State<String> = _username

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _showPassword = mutableStateOf(false)
    val showPassword: State<Boolean> = _showPassword

    private val _usernameError = mutableStateOf("")
    val usernameError : State<String> = _usernameError

    private val _passwordError = mutableStateOf("")
    val passwordError : State<String> = _passwordError

    fun setUsername(username: String){
        _username.value = username
    }

    fun setPassword(password: String){
        _password.value = password
    }

    fun setIsUsernameError(error: String){
        _usernameError.value = error
    }

    fun setIsPasswordError(error: String){
        _passwordError.value = error
    }

    fun setShowPassword(showPassword: Boolean){
        _showPassword.value = showPassword
    }
}