package com.femi.e_class.domain.use_case

class ValidateRepeatedPassword {

    fun execute(password: String, repeatedPassword: String): ValidationResult {

        if (password != repeatedPassword) {
            return ValidationResult(
                false,
                "Passwords don't match"
            )
        }
        return ValidationResult(
            true
        )
    }
}