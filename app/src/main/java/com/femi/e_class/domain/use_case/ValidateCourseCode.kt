package com.femi.e_class.domain.use_case

class ValidateCourseCode {

    fun execute(courseCode: String): ValidationResult{

        var intCounter = 0
        var letterCounter = 0
        for (c in courseCode) {
            if (c.isLetter()) {
                letterCounter++
            }
            if (c.isDigit()){
                intCounter++
            }
        }

        if (courseCode.isBlank()){
            return ValidationResult(
                false,
                "Course code can't be black"
            )
        }
        if (courseCode.length != 6){
            return ValidationResult(
                false,
                "Course code must be 6 characters long"
            )
        }
        if (intCounter != 3){
            return ValidationResult(
                false,
                "Course code must contain 3 numbers"
            )
        }
        if (letterCounter != 3){
            return ValidationResult(
                false,
                "Course code must contain 3 letters"
            )
        }
        return ValidationResult(
            true
        )
    }
}