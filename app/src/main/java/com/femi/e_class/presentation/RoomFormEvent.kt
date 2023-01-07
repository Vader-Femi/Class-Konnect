package com.femi.e_class.presentation

sealed class RoomFormEvent{
    data class CourseCodeChanged(val courseCode: String): RoomFormEvent()

    object Submit: RoomFormEvent()
}
