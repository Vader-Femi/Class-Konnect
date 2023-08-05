package com.femi.e_class.domain.use_case

class ValidateCourseCode() {

    fun execute(courseCode: String): ValidationResult {

        var letterCounter = 0
        var intCounter = 0

        if (courseCode.isBlank()) {
            return ValidationResult(
                false,
                "Course code can't be black"
            )
        }
        if (courseCode.length != 6) {
            return ValidationResult(
                false,
                "Course code must be 6 characters long"
            )
        }

        if (!courseCode.any{it.isLetter()} && !courseCode.any{it.isDigit()}) {
            return ValidationResult(
                false,
                "Course code must only contain 3 letters and 3 numbers"
            )
        }

        for (i in 0..2){
            if (courseCode[i].isLetter())
                letterCounter++
        }
        if (letterCounter != 3) {
            return ValidationResult(
                false,
                "Course code must start with 3 letters"
            )
        }

        for (i in 3..5){
            if (courseCode[i].isDigit())
                intCounter++
        }

        if (intCounter != 3) {
            return ValidationResult(
                false,
                "Course code must end with 3 numbers"
            )
        }
        return ValidationResult(
            true
        )
    }
}