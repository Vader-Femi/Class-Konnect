package com.femi.e_class.presentation.authentication.resetpassword

data class ResetPasswordFormState(
    val email: String = "",
    val emailError: String? = null
)
