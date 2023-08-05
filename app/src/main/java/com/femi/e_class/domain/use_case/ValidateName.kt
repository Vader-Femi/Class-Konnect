package com.femi.e_class.domain.use_case

class ValidateName() {

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