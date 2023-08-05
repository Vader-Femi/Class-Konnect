package com.femi.e_class.presentation.authentication.SignUp

sealed class SignUpFormEvent{
    data class FirstNameChanged(val firstName: String): SignUpFormEvent()
    data class LastNameChanged(val lastName: String): SignUpFormEvent()
    data class MatricChanged(val matric: String): SignUpFormEvent()
    data class EmailChanged(val email: String): SignUpFormEvent()
    data class PasswordChanged(val password: String): SignUpFormEvent()

    object Submit: SignUpFormEvent()
}
