package com.femi.e_class.domain.use_case

class ValidateLogInPassword() {

    fun execute(password: String): ValidationResult {

        if (password.isEmpty()) {
            return ValidationResult(
                false,
                "Password cannot be empty"
            )
        }
        return ValidationResult(
            true
        )
    }
}