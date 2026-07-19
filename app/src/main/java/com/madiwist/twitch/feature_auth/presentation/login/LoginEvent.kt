package com.madiwist.twitch.feature_auth.presentation.login

sealed class LoginEvent {
    data class EnteredEmail(val value: String) : LoginEvent()
    data class EnteredPassword(val value: String) : LoginEvent()
    object Login: LoginEvent()
    object TogglePasswordVisibility: LoginEvent()
}