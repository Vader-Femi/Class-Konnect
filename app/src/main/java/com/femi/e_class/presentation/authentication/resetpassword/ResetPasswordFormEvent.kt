package com.femi.e_class.presentation.authentication.resetpassword

sealed class ResetPasswordFormEvent{
    data class EmailChanged(val email: String): ResetPasswordFormEvent()

    object Submit: ResetPasswordFormEvent()
}
