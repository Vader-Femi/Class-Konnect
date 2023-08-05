package com.femi.e_class.domain.use_case

class ValidateSignUpPassword() {

    /**
     * Input will be invalid if:
     * Password is less that 8,
     * Password doesn't contain a letter A-Z or a-z,
     * Password doesn't contain a digit 0-9,
     * Password doesn't contain a special character, or
     * Password contains a space,
     */
    fun execute(password: String): ValidationResult {

        if (password.length < 8) {
            return ValidationResult(
                false,
                "Password cannot be less than 8 characters"
            )
        }
        if (!password.any{it.isLetter()}) {
            return ValidationResult(
                false,
                "Must contain at least one letter (A-Z or a-z)"
            )
        }
        if (!password.any{it.isDigit()}) {
            return ValidationResult(
                false,
                "Must contain at least one digit (0-9)"
            )
        }
        if (!password.any{!it.isDigit() && !it.isLetter()} ) {
            return ValidationResult(
                false,
                "Must contain at least 1 special character"
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