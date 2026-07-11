package com.madiwist.twitch.presentation.register

data class RegisterState(
    val username: String = "",
    val usernameError: UsernameError? = null,
    val email: String = "",
    val emailError: EmailError? = null,
    val password: String = "",
    val passwordError: PasswordError? = null,
    val isPasswordVisible: Boolean = false
) {
    sealed class UsernameError {
        object FieldEmpty : UsernameError()
        object InputTooShort : UsernameError()
    }

    sealed class EmailError {
        object FieldEmpty : EmailError()
        object InvalidEmail : EmailError()
    }

    sealed class PasswordError {
        object FieldEmpty : PasswordError()
        object InvalidPassword : PasswordError()
        object InputTooShort : PasswordError()
    }
}
