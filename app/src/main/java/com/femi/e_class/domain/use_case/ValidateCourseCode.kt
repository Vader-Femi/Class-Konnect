package com.femi.e_class.domain.use_case

class ValidateCourseCode {

    fun execute(roomName: String): ValidationResult{
        if (roomName.isBlank()){
            return ValidationResult(
                false,
                "Course code can't be black"
            )
        }
        if (roomName.length < 6){
            return ValidationResult(
                false,
                "Course code can't be less than 6 characters"
            )
        }
        if (!roomName.any{it.isDigit()}) {
            return ValidationResult(
                false,
                "Must contain at least one digit (0-9)"
            )
        }
        return ValidationResult(
            true
        )
    }
}