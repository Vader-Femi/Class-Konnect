package com.femi.e_class.presentation

sealed class RegistrationFormEvent{
    data class FirstNameChanged(val firstName: String): RegistrationFormEvent()
    data class LastNameChanged(val lastName: String): RegistrationFormEvent()
    data class MatricChanged(val matric: String): RegistrationFormEvent()
    data class EmailChanged(val email: String): RegistrationFormEvent()
    data class PasswordChanged(val password: String): RegistrationFormEvent()

    object Submit: RegistrationFormEvent()
}
