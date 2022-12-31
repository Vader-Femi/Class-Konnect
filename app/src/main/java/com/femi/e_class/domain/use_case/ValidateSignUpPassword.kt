package com.femi.e_class.domain.use_case

import android.app.Application

class ValidateSignUpPassword(val appContext: Application) {

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