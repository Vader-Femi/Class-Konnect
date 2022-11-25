package com.femi.e_class.presentation

sealed class RoomFormEvent{
//    data class RoomNameChanged(val roomName: String): RoomFormEvent()
    data class CourseCodeChanged(val courseCode: String): RoomFormEvent()
    data class PasswordChanged(val password: String): RoomFormEvent()

    object Submit: RoomFormEvent()
}
