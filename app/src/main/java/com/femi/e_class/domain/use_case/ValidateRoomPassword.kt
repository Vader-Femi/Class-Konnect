package com.femi.e_class.domain.use_case

class ValidateRoomPassword {

    fun execute(password: String): ValidationResult {

        if (password.length < 4) {
            return ValidationResult(
                false,
                "Password cannot be less than 4 characters"
            )
        }

        if (!password.any{it.isLetter()}) {
            return ValidationResult(
                false,
                "Must contain at least one letter (A-Z or a-z)"
            )
        }
        if (password.contains(" ")) {
            return ValidationResult(
                false,
                "Must no contain spaces"
            )
        }
        return ValidationResult(
            true
        )
    }
}