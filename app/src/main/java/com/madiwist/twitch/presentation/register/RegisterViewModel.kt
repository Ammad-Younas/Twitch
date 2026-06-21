package com.madiwist.twitch.presentation.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class RegisterViewModel @Inject constructor()  : ViewModel() {

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _username = mutableStateOf("")
    val username: State<String> = _username

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _emailError = mutableStateOf("")
    val emailError : State<String> = _emailError

    private val _usernameError = mutableStateOf("")
    val usernameError : State<String> = _usernameError

    private val _passwordError = mutableStateOf("")
    val passwordError : State<String> = _passwordError

    private val _showPassword = mutableStateOf(false)
    val showPassword: State<Boolean> = _showPassword

    fun setEmail(email: String){
        _email.value = email
    }

    fun setUsername(username: String){
        _username.value = username
    }

    fun setPassword(password: String){
        _password.value = password
    }

    fun setIsEmailError(error: String){
        _emailError.value = error
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