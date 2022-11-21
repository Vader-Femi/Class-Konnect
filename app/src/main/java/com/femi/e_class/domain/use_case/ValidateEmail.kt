package com.femi.e_class.domain.use_case

import android.util.Patterns

class ValidateEmail {

    fun execute(email: String): ValidationResult{
        if (email.isBlank()){
            return ValidationResult(
                false,
                "Email can't be black"
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return ValidationResult(
                false,
                "That's not a valid email"
            )
        }
        return ValidationResult(
            true
        )
    }
}