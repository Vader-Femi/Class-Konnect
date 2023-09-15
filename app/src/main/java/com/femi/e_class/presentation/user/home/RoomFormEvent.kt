package com.femi.e_class.presentation.user.home

sealed class RoomFormEvent{
    data class CourseCodeChanged(val courseCode: String): RoomFormEvent()

    object Submit: RoomFormEvent()
}
