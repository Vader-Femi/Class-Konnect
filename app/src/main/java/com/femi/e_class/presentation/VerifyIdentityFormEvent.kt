package com.femi.e_class.presentation

sealed class VerifyIdentityFormEvent{
    data class PasswordChanged(val password: String): VerifyIdentityFormEvent()

    object Submit: VerifyIdentityFormEvent()
}
