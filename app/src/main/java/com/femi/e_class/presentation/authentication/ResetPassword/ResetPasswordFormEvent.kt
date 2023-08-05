package com.femi.e_class.presentation.authentication.ResetPassword

sealed class ResetPasswordFormEvent{
    data class EmailChanged(val email: String): ResetPasswordFormEvent()

    object Submit: ResetPasswordFormEvent()
}
