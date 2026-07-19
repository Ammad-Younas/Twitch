package com.madiwist.twitch.core.domain.util

import android.util.Patterns
import com.madiwist.twitch.core.util.Constants
import com.madiwist.twitch.feature_auth.presentation.util.AuthError

object ValidationUtil {
    fun validateEmail(email: String) : AuthError? {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank()){
            return AuthError.FieldEmpty
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return AuthError.InvalidEmail
        }
        return null
    }

    fun validateUsername(username: String) : AuthError? {
        val trimmedUsername = username.trim()
        if (trimmedUsername.isBlank()){
            return AuthError.FieldEmpty
        }
        if (trimmedUsername.length < Constants.MIN_USERNAME_LENGTH){
            return AuthError.InputTooShort
        }
        return null
    }

    fun validatePassword(password: String) : AuthError? {
        if (password.isBlank()){
            return AuthError.FieldEmpty
        }
        if (password.length < Constants.MIN_PASSWORD_LENGTH){
            return AuthError.InputTooShort
        }
        val capitalLettersInPassword = password.any { it.isUpperCase() }
        val numbersInPassword = password.any { it.isDigit() }
        if (!capitalLettersInPassword || !numbersInPassword){
            return AuthError.InvalidPassword
        }
        return null
    }
}