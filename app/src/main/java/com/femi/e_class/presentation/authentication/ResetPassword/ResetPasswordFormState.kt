package com.femi.e_class.presentation.authentication.ResetPassword

data class ResetPasswordFormState(
    val email: String = "",
    val emailError: String? = null
)
