package com.femi.e_class.domain.use_case

import android.app.Application

class ValidateLogInPassword(val appContext: Application) {

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