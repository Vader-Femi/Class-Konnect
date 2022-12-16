package com.femi.e_class.presentation

sealed class ResetPasswordFormEvent{
    data class EmailChanged(val email: String): ResetPasswordFormEvent()

    object Submit: ResetPasswordFormEvent()
}
