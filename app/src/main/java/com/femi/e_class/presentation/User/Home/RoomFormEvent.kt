package com.femi.e_class.presentation.User.Home

sealed class RoomFormEvent{
    data class CourseCodeChanged(val courseCode: String): RoomFormEvent()

    object Submit: RoomFormEvent()
}
