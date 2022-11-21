package com.femi.e_class.presentation

sealed class SetPasswordFormEvent{
    data class PasswordChanged(val password: String): SetPasswordFormEvent()
    data class RepeatedPasswordChanged(val repeatedPassword: String): SetPasswordFormEvent()

    object Submit: SetPasswordFormEvent()
}
