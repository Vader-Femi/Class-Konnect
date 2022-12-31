package com.femi.e_class.presentation

data class RoomFormState(
    val courseCode: String = "",
    val courseCodeError: String? = null,
    val roomPassword: String = "",
    val roomPasswordError: String? = null,
)
