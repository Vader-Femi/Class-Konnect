package com.femi.e_class.domain.use_case

import android.app.Application

class ValidateName(val appContext: Application) {

    fun execute(name: String): ValidationResult{
        if (name.isBlank()){
            return ValidationResult(
                false,
                "Name can't be black"
            )
        }
        return ValidationResult(
            true
        )
    }
}