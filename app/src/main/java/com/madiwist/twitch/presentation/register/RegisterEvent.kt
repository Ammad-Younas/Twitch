package com.madiwist.twitch.presentation.register

sealed class RegisterEvent {
    data class EnteredUsername(val value: String) : RegisterEvent()
    data class EnteredEmail(val value: String) : RegisterEvent()
    data class EnteredPassword(val value: String) : RegisterEvent()
    object Register: RegisterEvent()
    object TogglePasswordVisibility: RegisterEvent()
}