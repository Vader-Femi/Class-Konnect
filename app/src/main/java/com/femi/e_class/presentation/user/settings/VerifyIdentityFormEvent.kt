package com.femi.e_class.presentation.user.settings

sealed class VerifyIdentityFormEvent{
    data class PasswordChanged(val password: String): VerifyIdentityFormEvent()

    object Submit: VerifyIdentityFormEvent()
}
