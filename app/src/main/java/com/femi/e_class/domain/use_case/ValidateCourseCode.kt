package com.femi.e_class.domain.use_case

import android.content.Context
import com.femi.e_class.R

class ValidateCourseCode(val context: Context) {

    fun execute(courseCode: String): ValidationResult {

        var letterCounter = 0
        var intCounter = 0

        if (courseCode.isBlank()) {
            return ValidationResult(
                false,
                context.getString(R.string.course_code_blank_error)
            )
        }
        if (courseCode.length != 6) {
            return ValidationResult(
                false,
                context.getString(R.string.course_code_too_long_error)
            )
        }

        if (!courseCode.any{it.isLetter()} && !courseCode.any{it.isDigit()}) {
            return ValidationResult(
                false,
                context.getString(R.string.course_code_not_just_letters_and_numbers_error)
            )
        }

        for (i in 0..2){
            if (courseCode[i].isLetter())
                letterCounter++
        }
        if (letterCounter != 3) {
            return ValidationResult(
                false,
                context.getString(R.string.course_code_not_3_letters_error)
            )
        }

        for (i in 3..5){
            if (courseCode[i].isDigit())
                intCounter++
        }

        if (intCounter != 3) {
            return ValidationResult(
                false,
                context.getString(R.string.course_code_not_3_numbers_error)
            )
        }
        return ValidationResult(
            true
        )
    }
}