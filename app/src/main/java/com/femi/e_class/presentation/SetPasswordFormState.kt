package com.femi.e_class.presentation

data class SetPasswordFormState(
    val password: String = "",
    val passwordError: String? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: String? = null,
)
