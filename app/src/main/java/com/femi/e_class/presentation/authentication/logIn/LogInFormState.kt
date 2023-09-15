package com.femi.e_class.presentation.authentication.logIn

data class LogInFormState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
)
