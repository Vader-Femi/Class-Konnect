package com.femi.e_class.presentation

sealed class RoomFormEvent{
//    data class RoomNameChanged(val roomName: String): RoomFormEvent()
    data class CourseCodeChanged(val courseCode: String): RoomFormEvent()
    data class RoomPasswordChanged(val roomPassword: String): RoomFormEvent()

    object Submit: RoomFormEvent()
}
