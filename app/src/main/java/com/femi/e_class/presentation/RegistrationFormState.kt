package com.femi.e_class.presentation

data class RegistrationFormState(
    val firstName: String = "",
    val firstNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val matric: String = "",
    val matricError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
//    val repeatedPassword: String = "",
//    val repeatedPasswordError: String? = null,
)
