package com.femi.e_class.domain.use_case

class ValidateRoomName {

    fun execute(roomName: String): ValidationResult{
        if (roomName.isBlank()){
            return ValidationResult(
                false,
                "Room name can't be black"
            )
        }
        if (roomName.length < 3){
            return ValidationResult(
                false,
                "Room name can't be less than 3 characters"
            )
        }
        return ValidationResult(
            true
        )
    }
}