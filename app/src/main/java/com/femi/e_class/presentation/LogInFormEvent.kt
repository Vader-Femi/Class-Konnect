package com.femi.e_class.presentation

sealed class LogInFormEvent{
    data class EmailChanged(val email: String): LogInFormEvent()
    data class PasswordChanged(val password: String): LogInFormEvent()

    object Submit: LogInFormEvent()
}
