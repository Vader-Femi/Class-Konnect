package com.femi.e_class.presentation.User.Profile

sealed class UpdateProfileFormEvent{
    data class FirstNameChanged(val firstName: String): UpdateProfileFormEvent()
    data class LastNameChanged(val lastName: String): UpdateProfileFormEvent()
    data class MatricChanged(val matric: String): UpdateProfileFormEvent()
    data class EmailChanged(val email: String): UpdateProfileFormEvent()

    object Submit: UpdateProfileFormEvent()
}
